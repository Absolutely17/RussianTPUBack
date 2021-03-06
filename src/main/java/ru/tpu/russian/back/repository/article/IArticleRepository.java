package ru.tpu.russian.back.repository.article;

import ru.tpu.russian.back.dto.article.ArticleCreateRequest;
import ru.tpu.russian.back.entity.Article;

import java.util.List;

public interface IArticleRepository {

    /**
     * Получаем список статей по ID пункта меню
     */
    List<Article> getBriefArticlesFromMenu(String menuItemId);

    /**
     * Создаем статью
     */
    String create(ArticleCreateRequest createDto);

    /**
     * Обновляем статью
     */
    void update(ArticleCreateRequest updateDto, String id);
}
