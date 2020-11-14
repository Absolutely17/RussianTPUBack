package ru.tpu.russian.back.dto.response;

import lombok.*;

/**
 * ДТО для админки
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
