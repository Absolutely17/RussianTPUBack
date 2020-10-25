package ru.tpu.russian.back.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

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

    public ArticleBriefResponse(String id, String topic, String briefText, String subject, String createDate) {
        this.id = id;
        this.topic = topic;
        this.briefText = briefText;
        this.subject = subject;
        this.createDate = createDate;
    }

    public void setArticleImage(byte[] articleImage) {
        this.articleImage = articleImage;
    }
}
