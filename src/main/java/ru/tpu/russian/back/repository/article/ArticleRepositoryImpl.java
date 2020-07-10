package ru.tpu.russian.back.repository.article;

import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.entity.*;

import javax.persistence.*;
import java.util.*;

public class ArticleRepositoryImpl implements IArticleRepository {

    private static final String PROCEDURE_GET_BRIEF_ARTICLES = "GetArticlesBrief";

    private static final String PROCEDURE_GET_BRIEF_ARTICLES_FROM_MENU = "GetArticlesBriefFromMenu";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Article> getBriefArticlesFromMenu(String idMenuItem) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_GET_BRIEF_ARTICLES_FROM_MENU);
        storedProcedureQuery.setParameter("menu_id", idMenuItem);
        storedProcedureQuery.execute();
        return storedProcedureQuery.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> getBriefArticles(String id) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_GET_BRIEF_ARTICLES);
        storedProcedureQuery.setParameter("str_id", id);
        storedProcedureQuery.execute();
        return storedProcedureQuery.getResultList();
    }

}
