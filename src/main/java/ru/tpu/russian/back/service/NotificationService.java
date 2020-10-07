package ru.tpu.russian.back.service;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.request.*;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.exception.ExceptionMessage;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.repository.notification.*;
import ru.tpu.russian.back.repository.user.UserRepository;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@Service
@Slf4j
public class NotificationService {

    public static final int MAX_USERS_ON_NOTIFICATION = 20;

    private final INotificationRepository notificationRepository;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final MailingTokenRepository mailingTokenRepository;

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private static final String TOPIC_NAME = "news_";

    public NotificationService(
            INotificationRepository notificationRepository,
            JwtProvider jwtProvider,
            UserRepository userRepository,
            MailingTokenRepository mailingTokenRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.mailingTokenRepository = mailingTokenRepository;
    }

    public ResponseEntity<?> sendOnGroup(NotificationRequestGroupDto request) {
        log.info("Check if the user is allowed to execute the request.");
        if (!isAllowed(request.getToken(), request.getEmail())) {
            return new ResponseEntity<>(
                    new ExceptionMessage("Unauthorized"),
                    FORBIDDEN
            );
        }
        log.info("Send notification on app.", request.toString());
        request.setTopic(TOPIC_NAME + request.getLanguage().toString());
        try {
            Message message = getSingleMessageBuilder(request)
                    .setTopic(request.getTopic())
                    .build();
            String response = sendSingleMessage(message);
            log.info("Notification send. Response {}", response);
            notificationRepository.createGroupNotification(convertGroupNotificationToMap(request));
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

    /**
     * Нам необходимо произвести проверку авторизации для доступа к запросу, так как запрос с админки
     * Этот случай не попадает под существующий фильтр, поэтому такая реализация
     *
     * @param token сгенерированный сервисом для админки
     * @param email администратора, который хочет отправить уведомление
     * @return boolean показываюищй разрешен доступ к запросу или нет
     */
    private boolean isAllowed(String token, String email) {
        if (token != null && jwtProvider.validateToken(token)) {
            String emailInToken = jwtProvider.getEmailFromToken(token);
            if (email.equals(emailInToken)) {
                Optional<User> user = userRepository.getUserByEmail(email);
                if (user.isPresent()) {
                    return ROLE_ADMIN.equals(user.get().getRole());
                }
            }
        }
        return false;
    }

    private Map<String, Object> convertGroupNotificationToMap(NotificationRequestGroupDto request) {
        Map<String, Object> params = new HashMap<>();
        params.put("Language", request.getLanguage().toString());
        params.put("Email", request.getEmail());
        params.put("Status", "Успешно");
        params.put("Title", request.getTitle());
        params.put("Message", request.getMessage());
        return params;
    }

    private String sendSingleMessage(Message message) throws ExecutionException, InterruptedException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private Message.Builder getSingleMessageBuilder(NotificationBaseRequestDto request) {
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

    public ResponseEntity<?> sendOnUser(NotificationRequestUsersDto requestDto) {
        log.info("Check if the user is allowed to execute the request.");
        if (!isAllowed(requestDto.getToken(), requestDto.getEmail())) {
            return new ResponseEntity<>(
                    new ExceptionMessage("Unauthorized"),
                    FORBIDDEN
            );
        }
        if (requestDto.getUsers().size() > MAX_USERS_ON_NOTIFICATION) {
            return new ResponseEntity<>(
                    new ExceptionMessage("Too many users have been selected for mailing."),
                    BAD_REQUEST
            );
        }
        log.info("Send notification on app.", requestDto.toString());
        try {
            List<String> userFcmTokens = requestDto.getUsers().stream()
                    .map(it -> mailingTokenRepository.getByUserId(it).getFcmToken())
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
            notificationRepository.createUsersNotification(convertUsersNotificationOnMap(requestDto, response));
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

    private Map<String, Object> convertUsersNotificationOnMap(NotificationRequestUsersDto request, String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("Users", request.toString());
        params.put("Title", request.getTitle());
        params.put("Message", request.getMessage());
        params.put("Email", request.getEmail());
        params.put("Status", status);
        return params;
    }

    private MulticastMessage.Builder getMulticastMessageBuilder(NotificationRequestUsersDto request) {
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
