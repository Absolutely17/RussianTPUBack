package ru.tpu.russian.back.dto.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import javax.validation.constraints.*;

/**
 * Уведомление группе пользователей по языку
 */
@AllArgsConstructor
@Getter
@Setter
public class NotificationRequestGroup extends NotificationBaseRequest {

    @NotNull
    private String language;

    @JsonCreator
    public NotificationRequestGroup(
            @NotNull String title, @NotNull String message,
            @NotNull @Email String adminEmail, String topic,
            @NotNull String language
    ) {
        super(title, message, adminEmail, topic);
        this.language = language;
    }
}
