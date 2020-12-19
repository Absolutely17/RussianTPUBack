package ru.tpu.russian.back.dto.article;

import lombok.*;

/**
 * ДТО статьи для админки таблицы
 */
@Getter
@AllArgsConstructor
public class ArticleTableRow {

    private String id;

    private String name;

    private String topic;

    private String language;

    private Integer countView;

    private String createDate;
}
