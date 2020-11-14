package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.SimpleNameObj;
import ru.tpu.russian.back.dto.article.*;
import ru.tpu.russian.back.dto.mapper.ArticleMapper;
import ru.tpu.russian.back.entity.Article;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.repository.article.ArticleRepository;
import ru.tpu.russian.back.repository.dicts.IDictRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ArticleService {

    private ArticleRepository articleRepository;

    private final MediaService mediaService;

    private final ArticleMapper articleMapper;

    private final IDictRepository dictRepository;

    public ArticleService(
            ArticleRepository articleRepository,
            MediaService mediaService,
            ArticleMapper articleMapper,
            IDictRepository dictRepository
    ) {
        this.articleRepository = articleRepository;
        this.mediaService = mediaService;
        this.articleMapper = articleMapper;
        this.dictRepository = dictRepository;
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
            throw new BusinessException("Exception.briefArticle.notFound");
        }
        log.info("Count brief articles {}", articles.size());
        return articles
                .stream()
                .map(this::convertToBriefResponse)
                .collect(toList());
    }

    private ArticleBriefResponse convertToBriefResponse(Article article) {
        ArticleBriefResponse response = articleMapper.convertBriefToResponse(article);
        if (article.getArticleImage() != null) {
            response.setArticleImage(mediaService.getImage(article.getArticleImage()));
        }
        return response;
    }

    @Transactional
    public ArticleResponse getArticle(String id) throws BusinessException {
        log.info("Get article. ID article = {}", id);
        Optional<Article> article = articleRepository.getById(id);
        if (article.isPresent()) {
            Integer countViews = article.get().getCountView();
            articleRepository.updateCountViews(id, countViews == null ? 0 : countViews);
            return articleMapper.convertToResponse(article.get());
        } else {
            log.error("Could not find article with id {}", id);
            throw new BusinessException("Exception.article.notFound");
        }
    }

    public List<ArticleRegistryResponse> getTable() {
        return articleRepository.findAll().stream()
                .map(articleMapper::convertToRegistryTable)
                .collect(toList());
    }

    public Map<String, List<SimpleNameObj>> getDictsForTable() {
        Map<String, List<SimpleNameObj>> dicts = new HashMap<>();
        List<SimpleNameObj> languages = dictRepository.getAllLanguage()
                .stream()
                .map(it -> new SimpleNameObj(it.getId(), it.getFullName()))
                .collect(Collectors.toList());
        dicts.put("languages", languages);
        return dicts;
    }

    public String create(ArticleCreateRequest createDto) {
        return articleRepository.create(createDto);
    }

    public void update(ArticleCreateRequest updateDto, String id) {
        articleRepository.update(updateDto, id);
    }
}
