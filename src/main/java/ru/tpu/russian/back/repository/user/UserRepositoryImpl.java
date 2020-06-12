package ru.tpu.russian.back.repository.user;

import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.entity.User;

import javax.persistence.*;
import java.util.*;

public class UserRepositoryImpl implements IUserRepository {

    private static final String PROCEDURE_GET_USER_BY_LANGUAGE = "GetUserByLanguage";

    private static final String PROCEDURE_GET_USER_BY_REG = "GetRegistered";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<User> getAllByLanguage(Map<String, Object> params) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_GET_USER_BY_LANGUAGE);
        for (String key : params.keySet()) {
            storedProcedureQuery.setParameter(key, params.get(key));
        }
        storedProcedureQuery.execute();
        return storedProcedureQuery.getResultList();
    }

    @Override
    @Transactional
    public List<User> getAllByReg(Map<String, Object> params) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_GET_USER_BY_REG);
        for (String key : params.keySet()) {
            storedProcedureQuery.setParameter(key, params.get(key));
        }
        storedProcedureQuery.execute();
        return storedProcedureQuery.getResultList();
    }
}
