package ru.tpu.russian.back.dto.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import ru.tpu.russian.back.enums.NotificationTargetGroup;

import javax.annotation.Nullable;
import javax.validation.constraints.*;

/**
 * Уведомление группе пользователей по языку
 */
@Getter
@Setter
@NoArgsConstructor
public class NotificationRequestGroup extends NotificationBaseRequest {

    @Nullable
    private NotificationTargetGroup targetGroup;

    @Nullable
    private String languageId;

    @JsonCreator
    public NotificationRequestGroup(
            @NotNull String title, @NotNull String message,
            @NotNull @Email String adminEmail, String topic,
            @Nullable NotificationTargetGroup targetGroup, @Nullable String languageId
    ) {
        super(title, message, adminEmail, topic);
        this.targetGroup = targetGroup;
        this.languageId = languageId;
    }

    @Override
    public String toString() {
        return "NotificationRequestGroup{" +
                "targetGroup='" + targetGroup + '\'' +
                ", languageId='" + languageId + '\'' +
                ", title='" + getTitle() + '\'' +
                ", message='" + getMessage() + '\'' +
                ", email='" + getAdminEmail() + '\'' +
                '}';
    }
}
