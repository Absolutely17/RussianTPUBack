package ru.tpu.russian.back.dto.article;

import lombok.*;

import java.util.List;

/**
 * Возвращаем список кратких версий статей с заголовком, объединяющим все эти статьи
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ArticleListResponse {

    private List<ArticleBriefResponse> articles;

    private String title;
}
