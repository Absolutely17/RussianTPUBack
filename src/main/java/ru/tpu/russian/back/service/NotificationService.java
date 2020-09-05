package ru.tpu.russian.back.service;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.request.NotificationRequestDto;
import ru.tpu.russian.back.exception.ExceptionMessage;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
@Slf4j
public class NotificationService {

    private static final String TOPIC_NAME = "news_";

    public ResponseEntity<?> send(NotificationRequestDto request) {
        log.info("Send notification on app.", request.toString());
        request.setTopic(TOPIC_NAME + request.getLanguage().toString());
        try {
            Message message = getPreConfiguredMessage(request);
            String response = sendAndGetResponse(message);
            log.info("Notification send. Response {}", response);
            return new ResponseEntity<>(
                    OK
            );
        } catch (ExecutionException | InterruptedException ex) {
            return new ResponseEntity<>(
                    "Problems sending notification",
                    INTERNAL_SERVER_ERROR
            );
        }
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
