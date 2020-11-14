package ru.tpu.russian.back.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
public class ArticleCreateRequest {

    private String name;

    private String topic;

    private String text;

    private String briefText;

    private String language;

    private String imageId;
}
