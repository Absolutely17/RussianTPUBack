package ru.tpu.russian.back.service;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.request.NotificationRequestDto;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.exception.ExceptionMessage;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.repository.notification.INotificationRepository;
import ru.tpu.russian.back.repository.user.UserRepository;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.*;

@Service
@Slf4j
public class NotificationService {

    private final INotificationRepository notificationRepository;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private static final String TOPIC_NAME = "news_";

    public NotificationService(
            INotificationRepository notificationRepository,
            JwtProvider jwtProvider,
            UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> send(NotificationRequestDto request) {
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
            Message message = getPreConfiguredMessage(request);
            String response = sendAndGetResponse(message);
            log.info("Notification send. Response {}", response);
            notificationRepository.createNotification(convertNotificationToMap(request));
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

    private Map<String, Object> convertNotificationToMap(NotificationRequestDto request) {
        Map<String, Object> params = new HashMap<>();
        params.put("Language", request.getLanguage().toString());
        params.put("Email", request.getEmail());
        params.put("Status", "Успешно");
        params.put("Title", request.getTitle());
        params.put("Message", request.getMessage());
        return params;
    }

    private String sendAndGetResponse(Message message) throws ExecutionException, InterruptedException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private Message getPreConfiguredMessage(NotificationRequestDto request) {
        return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(NotificationRequestDto request) {
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
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setTag(topic).build()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }
}
