package ru.tpu.russian.back.repository.notification;

import ru.tpu.russian.back.dto.notification.*;

public interface INotificationRepository {

    /**
     * Отправить групповое уведомление. Здесь сохраняем, дабы был лог
     */
    void createGroupNotification(NotificationRequestGroup request, String status);

    /**
     * Отправить уведомление конкретно выбранным пользователям. Здесь сохраняем, дабы был лог
     */
    void createUsersNotification(NotificationRequestUsers request, String status);
}
