package ru.tpu.russian.back.dto.article;

import lombok.*;

/**
 * ДТО полной статьи
 */
@Getter
@AllArgsConstructor
public class ArticleResponse {

    private String id;

    private String name;

    private String text;

    private String topic;

    private String createDate;
}
