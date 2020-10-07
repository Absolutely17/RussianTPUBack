package ru.tpu.russian.back.repository.notification;

import java.util.Map;

public interface INotificationRepository {

    void createGroupNotification(Map<String, Object> params);

    void createUsersNotification(Map<String, Object> params);
}
