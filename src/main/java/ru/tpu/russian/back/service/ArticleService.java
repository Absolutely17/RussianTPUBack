package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.*;
import ru.tpu.russian.back.entity.Article;
import ru.tpu.russian.back.repository.article.ArticleRepository;
import ru.tpu.russian.back.repository.media.MediaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private ArticleRepository articleRepository;

    private MediaRepository mediaRepository;

    public ArticleService(
            ArticleRepository articleRepository,
            MediaRepository mediaRepository
    ) {
        this.articleRepository = articleRepository;
        this.mediaRepository = mediaRepository;
    }

    public List<ArticleBriefResponse> getArticlesBrief(String id, boolean fromMenu) {
        List<Article> articles;
        if (fromMenu) {
            articles = articleRepository.getBriefArticlesFromMenu(id);
        } else {
            articles = articleRepository.getBriefArticles(id);
        }
        return articles
                .stream()
                .map(this::convertToBriefResponse)
                .collect(Collectors.toList());
    }

    private ArticleBriefResponse convertToBriefResponse(Article article) {
        ArticleBriefResponse response = new ArticleBriefResponse(article);
        if (article.getArticleImage() != null) {
            response.setArticleImage(mediaRepository.getById(article.getArticleImage()).getData());
        }
        return response;
    }

    public ArticleResponse getArticle(String id) {
        return new ArticleResponse(articleRepository.getById(id));
    }
}
