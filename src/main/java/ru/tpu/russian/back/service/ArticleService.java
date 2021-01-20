package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.SimpleNameObj;
import ru.tpu.russian.back.dto.article.*;
import ru.tpu.russian.back.entity.Article;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.mapper.ArticleMapper;
import ru.tpu.russian.back.repository.article.ArticleRepository;
import ru.tpu.russian.back.repository.language.LanguageRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ArticleService {

    private ArticleRepository articleRepository;

    private final MediaService mediaService;

    private final ArticleMapper articleMapper;

    private final LanguageRepository languageRepository;

    public ArticleService(
            ArticleRepository articleRepository,
            MediaService mediaService,
            ArticleMapper articleMapper,
            LanguageRepository languageRepository
    ) {
        this.articleRepository = articleRepository;
        this.mediaService = mediaService;
        this.articleMapper = articleMapper;
        this.languageRepository = languageRepository;
    }

    /**
     * Достаем все крактие версии статей
     *
     * @param id ID пункта меню
     */
    public List<ArticleBriefResponse> getArticlesBrief(String id) throws BusinessException {
        log.info("Get brief articles from menu. ID menu item = {}", id);
        List<Article> articles = articleRepository.getBriefArticlesFromMenu(id);
        if (articles == null || articles.isEmpty()) {
            log.error("Could not find articles. Menu item ID {}", id);
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

    /**
     * Достать полную версию статьи
     *
     * @param id ID статьи
     */
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

    /**
     * Получить таблицу со статьями
     */
    public List<ArticleTableRow> getTable() {
        return articleRepository.findAll().stream()
                .map(articleMapper::convertToTableRow)
                .collect(toList());
    }

    /**
     * Получить определенную статью для админки
     */
    public ArticleCreateRequest getArticleById(String id) {
        return articleRepository.findById(id).map(articleMapper::convertToFullArticle).orElse(null);
    }

    /**
     * Получить дополнительные материалы для таблицы статей
     */
    public Map<String, List<SimpleNameObj>> getDictsForTable() {
        Map<String, List<SimpleNameObj>> dicts = new HashMap<>();
        List<SimpleNameObj> languages = languageRepository.findAll()
                .stream()
                .map(it -> new SimpleNameObj(it.getId(), it.getName()))
                .collect(Collectors.toList());
        dicts.put("languages", languages);
        return dicts;
    }

    /**
     * Создание новой статьи
     *
     * @return ID новой статьи
     */
    public String create(ArticleCreateRequest createDto) {
        return articleRepository.create(createDto);
    }

    /**
     * Обновляем имеющуюся статью
     */
    public void update(ArticleCreateRequest updateDto, String id) {
        articleRepository.update(updateDto, id);
    }
}
