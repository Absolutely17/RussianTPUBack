package ru.tpu.russian.back.repository.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.entity.User;

import javax.persistence.*;
import java.util.*;

@Slf4j
public class UserRepositoryImpl implements IUserRepository {

    private static final String PROCEDURE_GET_USER_BY_LANGUAGE = "GetUserByLanguage";

    private static final String PROCEDURE_GET_USER_BY_REG = "GetRegistered";

    private static final String PROCEDURE_ADD_USER = "AddUser";

    private static final String PROCEDURE_EDIT_REFRESH_SALT = "EditUserRefreshSalt";

    private static final String PROCEDURE_GET_USER_BY_EMAIL = "GetUserByEmail";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllByLanguage(Map<String, Object> params) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_GET_USER_BY_LANGUAGE);
        for (String key : params.keySet()) {
            storedProcedureQuery.setParameter(key, params.get(key));
        }
        storedProcedureQuery.execute();
        return storedProcedureQuery.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllByReg(Map<String, Object> params) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_GET_USER_BY_REG);
        for (String key : params.keySet()) {
            storedProcedureQuery.setParameter(key, params.get(key));
        }
        storedProcedureQuery.execute();
        return storedProcedureQuery.getResultList();
    }

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
            return Optional.of((User) storedProcedureQuery.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
