package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.SimpleNameObj;
import ru.tpu.russian.back.dto.article.*;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.ArticleService;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/article")
public class ArticleRest {

    private ArticleService articleService;

    public ArticleRest(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RequestMapping(method = GET, path = "/list/{id}")
    public ArticleListResponse getArticlesBrief(@PathVariable String id) throws BusinessException {
        return articleService.getArticlesBrief(id);
    }

    @RequestMapping(method = GET)
    public ArticleResponse getArticle(
            @RequestParam(value = "id", required = false) String id
    ) throws BusinessException {
        return articleService.getArticle(id);
    }

    /**
     * РЕСТы для админки
     */
    @RequestMapping(method = GET, path = "/admin/table")
    public List<ArticleTableRow> getTable() {
        return articleService.getTable();
    }

    @RequestMapping(method = GET, path = "/admin/table/{id}")
    public ArticleCreateRequest getArticleById(@PathVariable String id) {
        return articleService.getArticleById(id);
    }

    @RequestMapping(method = GET, path = "/admin/dicts")
    public Map<String, List<SimpleNameObj>> getDicts() {
        return articleService.getDictsForTable();
    }

    @RequestMapping(method = POST, path = "/admin/create")
    public String create(@RequestBody ArticleCreateRequest createDto) {
        return articleService.create(createDto);
    }

    @RequestMapping(method = PUT, path = "/admin/{id}")
    public void update(@RequestBody ArticleCreateRequest updateDto, @PathVariable String id) {
        articleService.update(updateDto, id);
    }

    @RequestMapping(method = DELETE, path = "/admin/{id}")
    public void delete(
            @PathVariable String id
    ) {
        articleService.delete(id);
    }
}
