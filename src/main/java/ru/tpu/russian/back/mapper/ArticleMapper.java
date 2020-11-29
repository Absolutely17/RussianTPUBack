package ru.tpu.russian.back.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.article.*;
import ru.tpu.russian.back.entity.Article;

@Component
public class ArticleMapper {

    public ArticleBriefResponse convertBriefToResponse(Article article) {
        return new ArticleBriefResponse(
                article.getId(),
                article.getName(),
                article.getBriefText(),
                article.getTopic(),
                article.getCreateDate()
        );
    }

    public ArticleResponse convertToResponse(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getName(),
                article.getText(),
                article.getTopic(),
                article.getCreateDate()
        );
    }

    public ArticleTableRow convertToTableRow(Article article) {
        return new ArticleTableRow(
                article.getId(),
                article.getName(),
                article.getTopic(),
                article.getLanguage(),
                article.getCountView(),
                article.getCreateDate()
        );
    }

    public ArticleCreateRequest convertToFullArticle(Article article) {
        return new ArticleCreateRequest(article.getName(),
                article.getTopic(),
                article.getText(),
                article.getBriefText(),
                article.getLanguage(),
                article.getArticleImage()
        );
    }
}
