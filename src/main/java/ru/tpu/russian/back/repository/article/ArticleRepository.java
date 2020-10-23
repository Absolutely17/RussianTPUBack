package ru.tpu.russian.back.repository.article;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.Article;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String>, IArticleRepository {

    Optional<Article> getById(@Param("id") String id);

    @Modifying
    @Query("update Article SET countView = :count")
    void updateCountViews(@Param("count") Integer count);
}
