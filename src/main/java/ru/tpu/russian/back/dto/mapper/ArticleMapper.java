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
                article.getTopic(),
                article.getCreateDate()
        );
    }

    public ArticleResponse convertToResponse(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTopic(),
                article.getText(),
                article.getTopic(),
                article.getCreateDate()
        );
    }

    public ArticleRegistryResponse convertToRegistryTable(Article article) {
        return new ArticleRegistryResponse(
                article.getId(),
                article.getTopic(),
                article.getText(),
                article.getBriefText(),
                article.getTopic(),
                article.getLanguage(),
                article.getCountView(),
                article.getCreateDate()
        );
    }
}
