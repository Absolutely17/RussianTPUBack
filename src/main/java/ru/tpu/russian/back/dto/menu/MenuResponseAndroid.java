package ru.tpu.russian.back.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Пункт меню в ответ на получение пунктов меню
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class MenuResponseAndroid {

    private String id;

    private String name;

    private int level;

    private int position;

    private String type;

    @Nullable
    @Setter
    private String url;

    @Nullable
    private String idArticle;

    @Nullable
    @Setter
    private String image;

    @Nullable
    @Setter
    private String imageId;

    @Setter
    private List<MenuResponseAndroid> children;

    public MenuResponseAndroid(
            String id,
            String name,
            int level,
            int position,
            String type,
            @Nullable String url,
            @Nullable String idArticle,
            @Nullable String imageId
    ) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.position = position;
        this.type = type;
        this.url = url;
        this.idArticle = idArticle;
        this.imageId = imageId;
    }
}
