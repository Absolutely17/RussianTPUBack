package ru.tpu.russian.back.repository.article;

import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.entity.ArticleBrief;

import javax.persistence.*;
import java.util.*;

public class ArticleRepositoryImpl implements IArticleRepository {

    private static final String PROCEDURE_GET_BRIEF_ARTICLES = "GetArticlesBrief";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<ArticleBrief> getBriefArticles(Map<String, Object> params) {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_GET_BRIEF_ARTICLES);
        for (String key : params.keySet()) {
            storedProcedureQuery.setParameter(key, params.get(key));
        }
        storedProcedureQuery.execute();
        return storedProcedureQuery.getResultList();
    }

}
