package ru.tpu.russian.back.dto.notification;

import lombok.*;
import ru.tpu.russian.back.enums.NotificationTargetGroup;

import javax.annotation.Nullable;

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
