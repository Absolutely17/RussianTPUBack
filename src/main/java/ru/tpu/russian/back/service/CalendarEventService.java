package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.calendarEvent.*;
import ru.tpu.russian.back.dto.notification.*;
import ru.tpu.russian.back.entity.calendarEvent.*;
import ru.tpu.russian.back.entity.user.*;
import ru.tpu.russian.back.enums.*;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.mapper.CalendarEventMapper;
import ru.tpu.russian.back.repository.calendarEvent.CalendarEventRepository;
import ru.tpu.russian.back.repository.user.UserRepository;
import ru.tpu.russian.back.security.jwt.JwtProvider;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static ru.tpu.russian.back.enums.NotificationTargetGroup.ALL;

/**
 * Сервис по работе с событиями, задаваемыми в календаре у пользователя
 */
@Service
@Slf4j
public class CalendarEventService {

    private static final String TITLE_NOTIFICATION_CALENDAR_EVENT = "Вам назначено новое событие!";

    private static final String MESSAGE_NOTIFICATION_CALENDAR_EVENT = "%s\r\n%s\r\nЗагляните в календарь...";

    private final NotificationService notificationService;

    private final CalendarEventRepository calendarEventRepository;

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final CalendarEventMapper calendarEventMapper;

    public CalendarEventService(
            NotificationService notificationService,
            CalendarEventRepository calendarEventRepository,
            JwtProvider jwtProvider,
            CalendarEventMapper calendarEventMapper,
            UserRepository userRepository
    ) {
        this.notificationService = notificationService;
        this.calendarEventRepository = calendarEventRepository;
        this.jwtProvider = jwtProvider;
        this.calendarEventMapper = calendarEventMapper;
        this.userRepository = userRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Создать событие
     */
    @Transactional
    public void createEvent(CalendarEventCreateRequest request, String token) {
        log.info("Create calendar event. {}", request.toString());
        CalendarEvent event = calendarEventMapper.convertToCalendarEventFromRequest(request);
        // Для группы ALL мы не задаем в линковочную таблицу,
        // т.к. мы просто определяем по самой группе при доставании событий
        setEventTargets(request, event);
        if (request.isSendNotification()) {
            sendNotificationAboutCreatedEvent(request, token);
        }
        entityManager.persist(event);
        if (Strings.isNotEmpty(request.getDetailedMessage())) {
            CalendarEventDetailedMessage detailedMessage =
                    new CalendarEventDetailedMessage(request.getDetailedMessage());
            detailedMessage.setCalendarEvent(event);
            entityManager.persist(detailedMessage);
        }
    }

    private void setEventTargets(CalendarEventCreateRequest request, CalendarEvent event) {
        switch (event.getTargetEnum()) {
            case STUDY_GROUP:
                event.addNewTargets(request.getGroups()
                        .stream()
                        .map(it -> new CalendarEventTargets(event, entityManager.getReference(StudyGroup.class, it)))
                        .collect(Collectors.toSet()));
                break;
            case SELECTED_USERS:
                event.addNewTargets(request.getSelectedUsers()
                        .stream()
                        .map(it -> new CalendarEventTargets(event, entityManager.getReference(User.class, it)))
                        .collect(Collectors.toSet()));
                break;
        }
    }

    private void sendNotificationAboutCreatedEvent(CalendarEventCreateRequest request, String token) {
        NotificationBaseRequest requestNotification = createNotificationRequestFromTarget(
                request.getGroupTarget(),
                request,
                token
        );
        if (requestNotification instanceof NotificationRequestUsers) {
            notificationService.sendOnUser((NotificationRequestUsers)requestNotification);
        } else {
            notificationService.sendOnGroup((NotificationRequestGroup)requestNotification);
        }
    }

    private NotificationBaseRequest createNotificationRequestFromTarget(
            CalendarEventGroupTarget target,
            CalendarEventCreateRequest request,
            String token
    ) {
        NotificationBaseRequest requestNotification;
        switch (target) {
            case ALL:
                requestNotification = new NotificationRequestGroup();
                ((NotificationRequestGroup)requestNotification).setTargetGroup(ALL);
                break;
            case STUDY_GROUP:
                requestNotification = new NotificationRequestUsers();
                ((NotificationRequestUsers)requestNotification).setUsers(request.getGroups()
                        .stream()
                        .map(userRepository::getUsersByGroupId)
                        .collect(Collectors.toList())
                        .stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList()));
                break;
            case SELECTED_USERS:
                requestNotification = new NotificationRequestUsers();
                ((NotificationRequestUsers)requestNotification).setUsers(request.getSelectedUsers());
                break;
            default:
                throw new IllegalArgumentException("Wrong target calendar event value " + target.toString());
        }
        requestNotification.setTitle(TITLE_NOTIFICATION_CALENDAR_EVENT);
        requestNotification.setMessage(String.format(
                MESSAGE_NOTIFICATION_CALENDAR_EVENT,
                request.getTitle(), request.getDescription()
        ));
        requestNotification.setAdminEmail(jwtProvider.getEmailFromToken(jwtProvider.unwrapTokenFromHeaderStr(token)));
        requestNotification.setNotificationAppLink(NotificationAppLink.CALENDAR_EVENT);
        return requestNotification;
    }

    /**
     * Получить все события для пользователя
     */
    public List<CalendarEventResponse> getCalendarEvents(String token) {
        log.info("Get all calendar events");
        String email = jwtProvider.getEmailFromToken(jwtProvider.unwrapTokenFromHeaderStr(token));
        if (email != null) {
            return calendarEventRepository.findByUserEmail(email)
                    .stream()
                    .map(calendarEventMapper::convertCalendarEventToResponse)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Email in token is empty.");
        }
    }

    /**
     * Получить событие по ID
     */
    public CalendarEventDetailedResponse getDetailedCalendarEvent(String id) {
        log.info("Get detailed calendar event {}", id);
        Optional<CalendarEvent> event = calendarEventRepository.findById(id);
        if (event.isPresent()) {
            return calendarEventMapper.convertToDetailedResponse(event.get());
        } else {
            log.error("Calendar event with id {} doesnt exist", id);
            throw new BusinessException("Exception.user.calendarEvent.notFound");
        }
    }

    /**
     * Получить события для трех возможных таргета: группа, пользователь, все. Работает в админке.
     */
    public List<CalendarEventResponse> getEventsForAdmin(CalendarEventFetchRequest request) {
        List<CalendarEvent> events = new ArrayList<>();
        if (request.isFetchAll()) {
            events = calendarEventRepository.findAll();
        } else if (request.getGroupId() != null) {
            events = calendarEventRepository.findByGroupId(request.getGroupId());
        } else if (request.getUserId() != null) {
            events = calendarEventRepository.findByUserId(request.getUserId());
        }
        return events
                .stream()
                .map(calendarEventMapper::convertCalendarEventToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получаем подробную информацию по событию для админки
     */
    public CalendarEventDetailedResponseAdmin getDetailedCalendarEventForAdmin(String id) {
        log.info("Get detailed calendar event for admin {}", id);
        Optional<CalendarEvent> event = calendarEventRepository.getByIdWithEagerFetch(id);
        if (event.isPresent()) {
            return calendarEventMapper.convertToDetailedResponseForAdmin(event.get());
        } else {
            log.error("Calendar event with id {} doesnt exist", id);
            throw new BusinessException("Выбранное событие отсутствует.");
        }
    }

    /**
     * Изменить событие
     */
    @Transactional
    public void editEvent(String id, CalendarEventCreateRequest request, String tokenAdmin) {
        log.info("Editing existing event with id {}. {}", id, request.toString());
        Optional<CalendarEvent> event = calendarEventRepository.findById(id);
        if (event.isPresent()) {
            CalendarEvent editingEvent = event.get();
            editingEvent.setTitle(request.getTitle());
            editingEvent.setDescription(request.getDescription());
            editingEvent.setOnlineMeetingLink(request.getOnlineMeetingLink());
            editingEvent.setTimestamp(request.getTimestamp());
            editingEvent.setTargetEnum(request.getGroupTarget());
            if (Strings.isNotEmpty(request.getDetailedMessage())) {
                CalendarEventDetailedMessage detailedMessage;
                if (editingEvent.getDetailedMessage() != null) {
                    detailedMessage = editingEvent.getDetailedMessage();
                    detailedMessage.setMessage(request.getDetailedMessage());
                } else {
                    detailedMessage = new CalendarEventDetailedMessage(request.getDetailedMessage());
                    detailedMessage.setCalendarEvent(editingEvent);
                }
                entityManager.persist(detailedMessage);
            }
            if (request.isSendNotification()) {
                sendNotificationAboutCreatedEvent(request, tokenAdmin);
            }
            editingEvent.getTargets().clear();
            setEventTargets(request, editingEvent);
        } else {
            throw new BusinessException("Не удалось найти событие по указанному ID: " + id);
        }
    }
}
