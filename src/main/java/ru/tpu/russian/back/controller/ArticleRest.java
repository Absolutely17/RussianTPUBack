package ru.tpu.russian.back.controller;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.entity.ArticleBrief;
import ru.tpu.russian.back.service.ArticleService;

import javax.websocket.server.PathParam;
import java.util.List;

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

    @RequestMapping(method = GET, path = "/preview/{language}/{offset}/{count}")
    public List<ArticleBrief> getArticlesBrief(
            @PathVariable int offset,
            @PathVariable int count,
            @PathVariable String language
            ) {
        return articleService.getArticlesBrief(offset, count, language);
    }

}
