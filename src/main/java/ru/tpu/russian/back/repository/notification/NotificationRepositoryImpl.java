package ru.tpu.russian.back.repository.notification;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Map;

@Repository
public class NotificationRepositoryImpl implements INotificationRepository {

    private static final String PROCEDURE_CREATE_GROUP_NOTIFICATION = "AddGroupNotification";

    private static final String PROCEDURE_CREATE_USER_NOTIFICATION = "AddUsersNotification";

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createGroupNotification(Map<String, Object> params) {
        StoredProcedureQuery procedure = em.createNamedStoredProcedureQuery(PROCEDURE_CREATE_GROUP_NOTIFICATION);
        for (String key : params.keySet()) {
            procedure.setParameter(key, params.get(key));
        }
        procedure.execute();
    }

    @Override
    public void createUsersNotification(Map<String, Object> params) {
        StoredProcedureQuery procedure = em.createNamedStoredProcedureQuery(PROCEDURE_CREATE_USER_NOTIFICATION);
        for (String key : params.keySet()) {
            procedure.setParameter(key, params.get(key));
        }
        procedure.execute();
    }
}
