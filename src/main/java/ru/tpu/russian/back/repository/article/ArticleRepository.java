package ru.tpu.russian.back.repository.article;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.*;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String>, IArticleRepository {

    Article getById(@Param("id") String id) ;

}
