package ru.tpu.russian.back.repository.article;

import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.article.ArticleCreateRequest;
import ru.tpu.russian.back.entity.Article;

import javax.persistence.*;
import java.util.List;

public class ArticleRepositoryImpl implements IArticleRepository {

    private static final String GET_BRIEF_ARTICLES_FROM_MENU = "GetArticlesBriefFromMenu";

    private static final String CREATE_ARTICLE = "AddArticle";

    private static final String UPDATE_ARTICLE = "UpdateArticle";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Article> getBriefArticlesFromMenu(String menuItemId) {
        return em.createNativeQuery("exec " + GET_BRIEF_ARTICLES_FROM_MENU + " :menuItemId", Article.class)
                .setParameter("menuItemId", menuItemId)
                .getResultList();
    }

    @Override
    @Transactional
    public String create(ArticleCreateRequest createDto) {
        return (String) em.createNativeQuery("exec " + CREATE_ARTICLE + " :name, :text, :topic," +
                " :languageId, :briefText, :imageId")
                .setParameter("name", createDto.getName())
                .setParameter("text", createDto.getText())
                .setParameter("topic", createDto.getTopic())
                .setParameter("languageId", createDto.getLanguage())
                .setParameter("briefText", createDto.getBriefText())
                .setParameter("imageId", createDto.getImageId())
                .getSingleResult();
    }

    @Override
    @Transactional
    public void update(ArticleCreateRequest updateDto, String id) {
        em.createNativeQuery("exec " + UPDATE_ARTICLE + " :id, :name, :text, :topic," +
                " :languageId, :briefText, :imageId")
                .setParameter("id", id)
                .setParameter("name", updateDto.getName())
                .setParameter("text", updateDto.getText())
                .setParameter("topic", updateDto.getTopic())
                .setParameter("languageId", updateDto.getLanguage())
                .setParameter("briefText", updateDto.getBriefText())
                .setParameter("imageId", updateDto.getImageId())
                .executeUpdate();
    }
}
