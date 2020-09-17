package ru.tpu.russian.back.repository.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.entity.User;

import javax.persistence.*;
import java.util.*;

@Slf4j
public class UserRepositoryImpl implements IUserRepository {

    private static final String PROCEDURE_ADD_USER = "AddUser";

    private static final String PROCEDURE_EDIT_REFRESH_SALT = "EditUserRefreshSalt";

    private static final String PROCEDURE_GET_USER_BY_EMAIL = "GetUserByEmail";

    private static final String PROCEDURE_SET_REGISTERED_STATUS = "EditRegisteredStatus";

    private static final String PROCEDURE_EDIT_USER = "EditUser";

    private static final String PROCEDURE_RESET_AND_EDIT_PASS = "EditPasswordUser";

    private static final String PROCEDURE_ADD_RESET_PASSWORD_TOKEN = "AddResetPasswordRequest";

    private static final String PROCEDURE_GET_GROUP_ID = "GetUserGroupID";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void saveUser(Map<String, Object> params) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_ADD_USER);
        for (String key : params.keySet()) {
            storedProcedureQuery.setParameter(key, params.get(key));
        }
        storedProcedureQuery.execute();
    }

    @Override
    @Transactional
    public void editRefreshSalt(String email, String salt) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_EDIT_REFRESH_SALT);
        storedProcedureQuery.setParameter("Salt", salt);
        storedProcedureQuery.setParameter("email", email);
        storedProcedureQuery.execute();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_GET_USER_BY_EMAIL);
        storedProcedureQuery.setParameter("email", email);
        storedProcedureQuery.execute();
        Optional<User> optionalUser;
        try {
            return Optional.ofNullable((User) storedProcedureQuery.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public int editRegisteredStatus(String email, boolean status) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_SET_REGISTERED_STATUS);
        storedProcedureQuery.setParameter("email", email);
        storedProcedureQuery.setParameter("status", status);
        storedProcedureQuery.execute();
        return storedProcedureQuery.getUpdateCount();
    }

    @Override
    @Transactional
    public void editUser(Map<String, Object> params) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_EDIT_USER);
        for (String key : params.keySet()) {
            storedProcedureQuery.setParameter(key, params.get(key));
        }
        storedProcedureQuery.execute();
    }

    @Override
    @Transactional
    public int resetAndEditPassword(String email, String newPassword, String token) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_RESET_AND_EDIT_PASS);
        storedProcedureQuery.setParameter("email", email);
        storedProcedureQuery.setParameter("newPassword", newPassword);
        storedProcedureQuery.setParameter("token", token);
        storedProcedureQuery.execute();
        return storedProcedureQuery.getUpdateCount();
    }

    @Override
    @Transactional
    public void addResetToken(String email, String token) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_ADD_RESET_PASSWORD_TOKEN);
        storedProcedureQuery.setParameter("email", email);
        storedProcedureQuery.setParameter("token", token);
        storedProcedureQuery.execute();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> getGroupId(String email) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_GET_GROUP_ID);
        storedProcedureQuery.setParameter("email", email);
        storedProcedureQuery.execute();
        try {
            return Optional.ofNullable((String) storedProcedureQuery.getOutputParameterValue("internalGroupID"));
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
