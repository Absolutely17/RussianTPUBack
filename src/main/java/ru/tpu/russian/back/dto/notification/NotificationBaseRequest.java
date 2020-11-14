package ru.tpu.russian.back.dto.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;

/**
 * Базовое ДТО для уведомлений
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationBaseRequest {

    @NotNull
    private String title;

    @NotNull
    private String message;

    @NotNull
    @Email
    private String adminEmail;

    @JsonIgnore
    private String topic = "news";

    @Override
    public String toString() {
        return "NotificationBaseRequestDto{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", email='" + adminEmail + '\'' +
                '}';
    }
}
