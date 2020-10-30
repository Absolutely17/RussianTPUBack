package ru.tpu.russian.back.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
public class ArticleResponse {

    private String id;

    private String topic;

    private String text;

    private String subject;

    private String createDate;
}
