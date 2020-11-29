package ru.tpu.russian.back.service;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.notification.*;
import ru.tpu.russian.back.entity.notification.MailingToken;
import ru.tpu.russian.back.exception.ExceptionMessage;
import ru.tpu.russian.back.repository.dicts.IDictRepository;
import ru.tpu.russian.back.repository.notification.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@Service
@Slf4j
public class NotificationService {

    private static final int MAX_USERS_ON_NOTIFICATION = 50;

    private static final String TOPIC_NAME = "news";

    private final INotificationRepository notificationRepository;

    private final MailingTokenRepository mailingTokenRepository;

    private final IDictRepository dictRepository;

    public NotificationService(
            INotificationRepository notificationRepository,
            MailingTokenRepository mailingTokenRepository,
            IDictRepository dictRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.mailingTokenRepository = mailingTokenRepository;
        this.dictRepository = dictRepository;
    }

    /**
     * Отправка уведомлений группе пользователей. Группа определяется по языку в topic
     *
     * @param request данные для отправки уведомления
     * @return ответ, успешно или ошибки
     */
    public ResponseEntity<?> sendOnGroup(NotificationRequestGroup request) {
        log.info("Send notification on app.", request.toString());
        String shortNameLang = dictRepository.getLanguageById(request.getLanguage()).getShortName();
        request.setTopic(TOPIC_NAME + "_" + shortNameLang);
        try {
            Message message = getSingleMessageBuilder(request)
                    .setTopic(request.getTopic())
                    .build();
            String response = sendSingleMessage(message);
            log.info("Notification send. Response {}", response);
            notificationRepository.createGroupNotification(request, response);
            return new ResponseEntity<>(
                    OK
            );
        } catch (ExecutionException | InterruptedException ex) {
            return new ResponseEntity<>(
                    new ExceptionMessage("Problems sending notification"),
                    INTERNAL_SERVER_ERROR
            );
        }
    }

    private String sendSingleMessage(Message message) throws ExecutionException, InterruptedException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private Message.Builder getSingleMessageBuilder(NotificationBaseRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig)
                .setNotification(
                        Notification.builder()
                                .setTitle(request.getTitle())
                                .setBody(request.getMessage())
                                .build());
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2L).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setTag(topic)
                        .build())
                .build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    /**
     * Отправка уведомлений выбранным пользователям.
     *
     * @param requestDto данные уведомления
     * @return ответ, успешно или ошибки
     */
    public ResponseEntity<?> sendOnUser(NotificationRequestUsers requestDto) {
        if (requestDto.getUsers().size() > MAX_USERS_ON_NOTIFICATION) {
            return new ResponseEntity<>(
                    new ExceptionMessage("Too many users have been selected for mailing."),
                    BAD_REQUEST
            );
        }
        log.info("Send notification on app.", requestDto.toString());
        try {
            List<String> userFcmTokens = requestDto.getUsers().stream()
                    .map(mailingTokenRepository::getByUserId)
                    .filter(MailingToken::isActive)
                    .map(MailingToken::getFcmToken)
                    .collect(toList());
            String response;
            if (userFcmTokens.size() > 1) {
                MulticastMessage message = getMulticastMessageBuilder(requestDto)
                        .addAllTokens(userFcmTokens)
                        .build();
                int countFailure = sendOnUsersAndGetFailureCount(message);
                response = countFailure == 0 ? "Успешно" : "Ошибка";
            } else if (userFcmTokens.size() == 1) {
                Message message = getSingleMessageBuilder(requestDto)
                        .setToken(userFcmTokens.get(0))
                        .build();
                sendSingleMessage(message);
                response = "Успешно";
            } else {
                return new ResponseEntity<>(
                        new ExceptionMessage("Empty recipient list"),
                        INTERNAL_SERVER_ERROR
                );
            }
            log.info("Notification send. Response {}", response);
            notificationRepository.createUsersNotification(requestDto, response);
            return new ResponseEntity<>(
                    OK
            );
        } catch (ExecutionException | InterruptedException | FirebaseMessagingException ex) {
            return new ResponseEntity<>(
                    new ExceptionMessage("Problems sending notification"),
                    INTERNAL_SERVER_ERROR
            );
        }
    }

    private MulticastMessage.Builder getMulticastMessageBuilder(NotificationRequestUsers request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        return MulticastMessage.builder()
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig)
                .setNotification(
                        Notification.builder()
                                .setTitle(request.getTitle())
                                .setBody(request.getMessage())
                                .build()
                );
    }

    private int sendOnUsersAndGetFailureCount(MulticastMessage message) throws FirebaseMessagingException {
        return FirebaseMessaging.getInstance().sendMulticast(message).getFailureCount();
    }
}
