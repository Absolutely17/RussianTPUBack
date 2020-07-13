package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.dto.*;
import ru.tpu.russian.back.service.ArticleService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/article", produces = APPLICATION_JSON_UTF8_VALUE)
@Api(tags = {SpringFoxConfig.ARTICLE_REST})
public class ArticleRest {

    private ArticleService articleService;

    public ArticleRest(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * Получение списка статей
     */
    @ApiOperation(value = "Получить список статей")
    @RequestMapping(method = GET, path = "/list/{id}")
    public List<ArticleBriefResponse> getArticlesBriefFromMenuItem(
            @ApiParam(value = "ID пункта меню (если fromMenu=true) или ID страницы (если fromMenu=false)", required = true)
            @PathVariable String id,
            @ApiParam(value = "Осуществлен ли переход из меню")
            @RequestParam(value = "fromMenu", defaultValue = "false") boolean fromMenu
            ) {
        return articleService.getArticlesBrief(id, fromMenu);
    }

    /**
     * Получение полной версии статьи
     */
    @ApiOperation(value = "Получение полной версии статьи")
    @RequestMapping(method = GET, path = "/{id}")
    public ArticleResponse getArticle(
            @ApiParam(value = "ID статьи", required = true)
            @PathVariable String id
    ) {
        return articleService.getArticle(id);
    }

}
