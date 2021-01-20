package ru.tpu.russian.back.dto.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

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
    private String targetGroupName;

    @Nullable
    private String languageId;

    @JsonCreator
    public NotificationRequestGroup(
            @NotNull String title, @NotNull String message,
            @NotNull @Email String adminEmail, String topic,
            @Nullable String targetGroupName, @Nullable String languageId
    ) {
        super(title, message, adminEmail, topic);
        this.targetGroupName = targetGroupName;
        this.languageId = languageId;
    }

    @Override
    public String toString() {
        return "NotificationRequestGroup{" +
                "targetGroupName='" + targetGroupName + '\'' +
                ", languageId='" + languageId + '\'' +
                ", title='" + getTitle() + '\'' +
                ", message='" + getMessage() + '\'' +
                ", email='" + getAdminEmail() + '\'' +
                '}';
    }
}
