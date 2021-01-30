package ru.tpu.russian.back.dto.notification;

import lombok.*;

/**
 * ДТО ответа уведомления
 */
@AllArgsConstructor
@Getter
public class NotificationResponse {

    private String title;

    private String message;

    private String sendDate;
}
