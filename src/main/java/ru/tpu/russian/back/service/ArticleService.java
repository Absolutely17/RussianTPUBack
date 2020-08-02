package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.response.*;
import ru.tpu.russian.back.entity.Article;
import ru.tpu.russian.back.repository.article.ArticleRepository;
import ru.tpu.russian.back.repository.media.MediaRepository;

import javax.persistence.NoResultException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
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
            log.info("Get brief articles from menu. ID menu item = {}", id);
            articles = articleRepository.getBriefArticlesFromMenu(id);
        } else {
            log.info("Get brief articles from page. ID page = {}", id);
            articles = articleRepository.getBriefArticles(id);
        }
        if (articles == null || articles.isEmpty()) {
            throw new NoResultException("Could not find articles.");
        }
        log.info("Count brief articles {}", articles.size());
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
        log.info("Get article. ID article = {}", id);
        Optional<Article> article = articleRepository.getById(id);
        return new ArticleResponse(articleRepository.getById(id)
                .orElseThrow(() -> new NoResultException("Wrong ID article.")));
    }
}
