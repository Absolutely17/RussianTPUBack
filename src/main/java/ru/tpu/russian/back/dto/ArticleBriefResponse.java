package ru.tpu.russian.back.dto;

import io.swagger.annotations.*;
import ru.tpu.russian.back.entity.Article;


public class ArticleBriefResponse {

    @ApiModelProperty(notes = "ID статьи, генерируется БД")
    private String id;

    @ApiModelProperty(notes = "Заголовок статьи")
    private String topic;

    @ApiModelProperty(notes = "Краткий текст статьи")
    private String briefText;

    @ApiModelProperty(notes = "Тема статьи")
    private String subject;

    @ApiModelProperty(notes = "Дата создания статьи, генерируется БД")
    private String createDate;

    public ArticleBriefResponse(Article article) {
        id = article.getId();
        topic = article.getTopic();
        briefText = article.getBriefText();
        subject = article.getSubject();
        createDate = article.getCreateDate();
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getBriefText() {
        return briefText;
    }

    public String getSubject() {
        return subject;
    }

    public String getCreateDate() {
        return createDate;
    }
}
