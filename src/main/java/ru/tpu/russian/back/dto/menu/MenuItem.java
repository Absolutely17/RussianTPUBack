package ru.tpu.russian.back.dto.menu;

import lombok.*;
import org.springframework.lang.Nullable;

/**
 * Общая ДТО для пункта меню. Используется для админки.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {

    @Setter
    private String id;

    private String name;

    private Integer level;

    private Integer position;

    private String languageId;

    @Setter
    @Nullable
    private String parentId;

    private String type;

    private String url;

    private String articleId;

    private String imageId;
}
