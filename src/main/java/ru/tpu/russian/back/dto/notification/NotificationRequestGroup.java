package ru.tpu.russian.back.dto.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Уведомление группе пользователей по языку
 */
@AllArgsConstructor
@Getter
@Setter
public class NotificationRequestGroup extends NotificationBaseRequest {

    @NotNull
    private String targetParameter;

    @JsonCreator
    public NotificationRequestGroup(
            @NotNull String title, @NotNull String message,
            @NotNull @Email String adminEmail, String topic,
            @NotNull String targetParameter
    ) {
        super(title, message, adminEmail, topic);
        this.targetParameter = targetParameter;
    }
}
