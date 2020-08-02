package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.service.ArticleService;

import javax.persistence.NoResultException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/article")
@Api(tags = {SpringFoxConfig.ARTICLE_REST})
public class ArticleRest {

    private ArticleService articleService;

    public ArticleRest(ArticleService articleService) {
        this.articleService = articleService;
    }

    @ApiOperation(value = "Получить список статей")
    @RequestMapping(method = GET, path = "/list/{id}")
    public ResponseEntity<?> getArticlesBriefFromMenuItem(
            @ApiParam(value = "ID пункта меню (если fromMenu=true) или ID страницы (если fromMenu=false)",
                    required = true)
            @PathVariable String id,
            @ApiParam(value = "Осуществлен ли переход из меню")
            @RequestParam(value = "fromMenu", defaultValue = "false") boolean fromMenu
    ) {
        try {
            return new ResponseEntity<>(
                    articleService.getArticlesBrief(id, fromMenu), OK
            );
        } catch (NoResultException ex) {
            return new ResponseEntity<>(
                    ex.getMessage(), BAD_REQUEST
            );
        }
    }

    @ApiOperation(value = "Получение полной версии статьи")
    @RequestMapping(method = GET, path = "/{id}")
    public ResponseEntity<?> getArticle(
            @ApiParam(value = "ID статьи", required = true)
            @PathVariable String id
    ) {
        try {
            return new ResponseEntity<>(
                    articleService.getArticle(id), OK
            );
        } catch (NoResultException ex) {
            return new ResponseEntity<>(
                    ex.getMessage(), BAD_REQUEST
            );
        }
    }

}
