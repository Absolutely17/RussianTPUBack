package ru.tpu.russian.back.dto;

import ru.tpu.russian.back.entity.Article;


public class ArticleBriefResponse {

    private String id;

    private String topic;

    private String briefText;

    private String subject;

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
