package ru.tpu.russian.back.repository.notification;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.notification.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    /**
     * Выбрать уведомления по эл. почте пользователя
     */
    @Query(value = "select * from NOTIFICATION main" +
            " JOIN NOTIFICATION_USER_LINK link ON link.NOTIFICATION_ID = main.ID" +
            " JOIN USER_EMAIL email ON email.USER_ID = link.USER_ID WHERE email.EMAIL = :email", nativeQuery = true)
    List<Notification> getAllByUser(@Param(value = "email") String email);
}
