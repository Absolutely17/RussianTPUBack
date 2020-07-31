package ru.tpu.russian.back.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import ru.tpu.russian.back.entity.Article;

@Getter
@AllArgsConstructor
public class ArticleResponse {

    @ApiModelProperty(value = "ID статьи, генерируется БД")
    private String id;

    @ApiModelProperty(value = "Заголовок статьи")
    private String topic;

    @ApiModelProperty(value = "Полный текст статьи")
    private String text;

    @ApiModelProperty(value = "Тематика статьи")
    private String subject;

    @ApiModelProperty(value = "Дата создания статьи, генерируется БД")
    private String createDate;

    public ArticleResponse(Article article) {
        id = article.getId();
        topic = article.getTopic();
        text = article.getText();
        subject = article.getSubject();
        createDate = article.getCreateDate();
    }
}