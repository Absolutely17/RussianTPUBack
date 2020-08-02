package ru.tpu.russian.back.repository.menu;

import ru.tpu.russian.back.entity.Menu;

import javax.persistence.*;
import java.util.*;

public class MenuRepositoryImpl implements IMenuRepository {

    private static final String PROCEDURE_GET_MENU = "GetMenuByLanguage";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Menu> getAll(Map<String, Object> params) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_GET_MENU);
        for (String key : params.keySet()) {
            storedProcedureQuery.setParameter(key, params.get(key));
        }
        storedProcedureQuery.execute();
        return storedProcedureQuery.getResultList();
    }
}
