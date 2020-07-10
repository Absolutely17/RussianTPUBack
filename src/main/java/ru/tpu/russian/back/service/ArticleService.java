package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.*;
import ru.tpu.russian.back.entity.*;
import ru.tpu.russian.back.repository.article.ArticleRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public PageDto<ArticleBriefResponse> getArticlesBrief(String id, boolean fromMenu) {
        List<Article> articles;
        if (fromMenu) {
            articles = articleRepository.getBriefArticlesFromMenu(id);
        } else {
            articles = articleRepository.getBriefArticles(id);
        }
        return new PageDto<>(
                (long) articles.size(),
                articles
                        .stream()
                        .map(ArticleBriefResponse::new)
                        .collect(Collectors.toList()));
    }

    public ArticleResponse getArticle(String id) {
        return new ArticleResponse(articleRepository.getById(id));
    }
}
