package ru.tpu.russian.back.repository.article;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.Article;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String>, IArticleRepository {

    /**
     * Получение полной версии статьи. Сделал напрямую, дабы не терять время на процедуру
     *
     * @param id ID статьи
     * @return полная статья
     */
    Optional<Article> getById(@Param("id") String id);

    /**
     * Обновляем счетчик просмотров при каждом обращении к статье
     *
     * @param id    ID статьи
     * @param count текущий счетчик
     */
    @Modifying
    @Query("update Article SET countView = (:count + 1) where id = :id")
    void updateCountViews(@Param("id") String id, @Param("count") int count);
}
