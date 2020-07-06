package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.ArticleBrief;
import ru.tpu.russian.back.repository.article.ArticleRepository;

import java.util.*;

@Service
public class ArticleService {

    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticleBrief> getArticlesBrief(int offset, int count, String language) {
        Map<String, Object> params = new HashMap<>();
        params.put("Offset", offset);
        params.put("Count", count);
        params.put("Lang", language);
        return articleRepository.getBriefArticles(params);
    }

}
