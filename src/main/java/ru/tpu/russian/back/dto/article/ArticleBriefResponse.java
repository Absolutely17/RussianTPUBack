package ru.tpu.russian.back.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

/**
 * Возвращаем краткую версию статьи
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ArticleBriefResponse {

    private String id;

    private String name;

    private String briefText;

    private String topic;

    private String createDate;

    private byte[] articleImage;

    public ArticleBriefResponse(String id, String name, String briefText, String topic, String createDate) {
        this.id = id;
        this.name = name;
        this.briefText = briefText;
        this.topic = topic;
        this.createDate = createDate;
    }

    public void setArticleImage(byte[] articleImage) {
        this.articleImage = articleImage;
    }
}
