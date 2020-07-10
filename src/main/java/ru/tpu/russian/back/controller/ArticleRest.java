package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.*;
import ru.tpu.russian.back.service.ArticleService;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/article", produces = APPLICATION_JSON_UTF8_VALUE)
public class ArticleRest {

    private ArticleService articleService;

    public ArticleRest(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * Получение списка статей
     */
    @RequestMapping(method = GET, path = "/list/{id}")
    public PageDto<ArticleBriefResponse> getArticlesBriefFromMenuItem(
            @PathVariable String id,
            @RequestParam(value = "fromMenu", defaultValue = "false") boolean fromMenu
            ) {
        return articleService.getArticlesBrief(id, fromMenu);
    }

    /**
     * Получение полной версии статьи
     */
    @RequestMapping(method = GET, path = "/{id}")
    public ArticleResponse getArticle(
            @PathVariable String id
    ) {
        return articleService.getArticle(id);
    }

}
