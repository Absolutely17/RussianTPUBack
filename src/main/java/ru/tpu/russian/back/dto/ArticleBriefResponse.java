package ru.tpu.russian.back.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import ru.tpu.russian.back.entity.Article;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
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

    public void setArticleImage(byte[] articleImage) {
        this.articleImage = articleImage;
    }
}
