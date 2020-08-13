package ru.tpu.russian.back.repository.menu;

import ru.tpu.russian.back.entity.Menu;

import javax.persistence.*;
import java.util.List;

public class MenuRepositoryImpl implements IMenuRepository {

    private static final String PROCEDURE_GET_MENU = "GetMenuByLanguage";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Menu> getAll(String language) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_GET_MENU);
        storedProcedureQuery.setParameter("Language", language);
        ;
        storedProcedureQuery.execute();
        return storedProcedureQuery.getResultList();
    }
}
