package ru.tpu.russian.back.repository.notification;

import java.util.Map;

public interface INotificationRepository {

    void createNotification(Map<String, Object> params);
}
