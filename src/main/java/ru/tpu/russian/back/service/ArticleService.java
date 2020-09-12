package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.response.*;
import ru.tpu.russian.back.entity.Article;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.repository.article.ArticleRepository;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ArticleService {

    private ArticleRepository articleRepository;

    private final MediaService mediaService;

    public ArticleService(
            ArticleRepository articleRepository,
            MediaService mediaService
    ) {
        this.articleRepository = articleRepository;
        this.mediaService = mediaService;
    }

    public List<ArticleBriefResponse> getArticlesBrief(String id, boolean fromMenu) throws BusinessException {
        List<Article> articles;
        if (fromMenu) {
            log.info("Get brief articles from menu. ID menu item = {}", id);
            articles = articleRepository.getBriefArticlesFromMenu(id);
        } else {
            log.info("Get brief articles from page. ID page = {}", id);
            articles = articleRepository.getBriefArticles(id);
        }
        if (articles == null || articles.isEmpty()) {
            log.error("Could not find articles. FromMenu {}, id {}", fromMenu, id);
            throw new BusinessException("Exception.briefArticle.notFound", id);
        }
        log.info("Count brief articles {}", articles.size());
        return articles
                .stream()
                .map(this::convertToBriefResponse)
                .collect(toList());
    }

    private ArticleBriefResponse convertToBriefResponse(Article article) {
        ArticleBriefResponse response = new ArticleBriefResponse(article);
        if (article.getArticleImage() != null) {
            response.setArticleImage(mediaService.getImage(article.getArticleImage()));
        }
        return response;
    }

    public ArticleResponse getArticle(String id) throws BusinessException {
        log.info("Get article. ID article = {}", id);
        Optional<Article> article = articleRepository.getById(id);
        if (article.isPresent()) {
            return new ArticleResponse(article.get());
        } else {
            log.error("Could not find article with id {}", id);
            throw new BusinessException("Exception.article.notFound", id);
        }
    }
}
