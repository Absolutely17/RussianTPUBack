package ru.tpu.russian.back.dto.notification;

import lombok.*;

/**
 * ДТО для сохранения токена пользователя для последующей отправки уведомления
 */
@AllArgsConstructor
@Setter
@Getter
public class NotificationTokenRequest {

    private String email;

    private String token;
}
