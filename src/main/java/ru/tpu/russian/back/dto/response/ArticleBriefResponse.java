package ru.tpu.russian.back.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ArticleBriefResponse {

    private String id;

    private String topic;

    private String briefText;

    private String subject;

    private String createDate;

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
