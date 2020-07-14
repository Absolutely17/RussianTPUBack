package ru.tpu.russian.back.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import ru.tpu.russian.back.entity.Article;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @ApiModelProperty(notes = "Картинка статьи")
    private byte[] articleImage;

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

    public byte[] getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(byte[] articleImage) {
        this.articleImage = articleImage;
    }
}
