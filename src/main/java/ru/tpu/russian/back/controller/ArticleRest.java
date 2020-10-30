package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.response.*;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.ArticleService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/article")
public class ArticleRest {

    private ArticleService articleService;

    public ArticleRest(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RequestMapping(method = GET, path = "/list/{id}")
    public List<ArticleBriefResponse> getArticlesBriefFromMenuItem(
            @PathVariable String id,
            @RequestParam(value = "fromMenu", defaultValue = "false") boolean fromMenu
    ) throws BusinessException {
        return articleService.getArticlesBrief(id, fromMenu);
    }

    @RequestMapping(method = GET, path = "/{id}")
    public ArticleResponse getArticle(
            @PathVariable String id
    ) throws BusinessException {
        return articleService.getArticle(id);
    }

}
