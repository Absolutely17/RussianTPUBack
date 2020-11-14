package ru.tpu.russian.back.dto.article;

import lombok.*;

/**
 * ДТО статьи для админки
 */
@Getter
@AllArgsConstructor
public class ArticleRegistryResponse {

    private String id;

    private String name;

    private String text;

    private String briefText;

    private String topic;

    private String language;

    private Integer countView;

    private String createDate;
}
