package ru.tpu.russian.back.repository.notification;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.notification.*;

import javax.persistence.*;

@Repository
public class NotificationRepositoryImpl implements INotificationRepository {

    private static final String CREATE_GROUP_NOTIFICATION = "AddGroupNotification";

    private static final String CREATE_USER_NOTIFICATION = "AddUsersNotification";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void createGroupNotification(NotificationRequestGroup request, String status) {
        em.createNativeQuery("exec " + CREATE_GROUP_NOTIFICATION +
                " :title, :message, :status, :adminEmail, :language")
                .setParameter("title", request.getTitle())
                .setParameter("message", request.getMessage())
                .setParameter("status", status)
                .setParameter("adminEmail", request.getAdminEmail())
                .setParameter("language", request.getTargetGroupName())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void createUsersNotification(NotificationRequestUsers request, String status) {
        em.createNativeQuery("exec " + CREATE_USER_NOTIFICATION +
                " :title, :message, :status, :adminEmail, :users")
                .setParameter("title", request.getTitle())
                .setParameter("message", request.getMessage())
                .setParameter("status", status)
                .setParameter("adminEmail", request.getAdminEmail())
                .setParameter("users", request.toString())
                .executeUpdate();
    }
}
