package ru.tpu.russian.back.repository.notification;

import ru.tpu.russian.back.dto.request.*;

public interface INotificationRepository {

    /**
     * Отправить групповое уведомление. Здесь сохраняем, дабы был лог
     */
    void createGroupNotification(NotificationRequestGroupDto request, String status);

    /**
     * Отправить уведомление конкретно выбранным пользователям. Здесь сохраняем, дабы был лог
     */
    void createUsersNotification(NotificationRequestUsersDto request, String status);
}
