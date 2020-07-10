package ru.tpu.russian.back.dto;

import ru.tpu.russian.back.entity.Article;

public class ArticleResponse {

    private String id;

    private String topic;

    private String text;

    private String subject;

    private String createDate;


    public ArticleResponse(Article article) {
        id = article.getId();
        topic = article.getTopic();
        text = article.getText();
        subject = article.getSubject();
        createDate = article.getCreateDate();
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getText() {
        return text;
    }

    public String getSubject() {
        return subject;
    }

    public String getCreateDate() {
        return createDate;
    }
}
