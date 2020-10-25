package ru.tpu.russian.back.dto.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.response.*;
import ru.tpu.russian.back.entity.Article;

@Component
public class ArticleMapper {

    public ArticleBriefResponse convertBriefToResponse(Article article) {
        return new ArticleBriefResponse(
                article.getId(),
                article.getTopic(),
                article.getBriefText(),
                article.getSubject(),
                article.getCreateDate()
        );
    }

    public ArticleResponse convertToResponse(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTopic(),
                article.getText(),
                article.getSubject(),
                article.getCreateDate()
        );
    }
}
