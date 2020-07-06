package ru.tpu.russian.back.repository.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.ArticleBrief;

import java.util.*;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleBrief, String>, IArticleRepository {
}
