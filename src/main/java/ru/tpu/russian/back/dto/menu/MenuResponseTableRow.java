package ru.tpu.russian.back.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuResponseTableRow {

    private String id;

    private String name;

    private int level;

    private int position;

    private String type;

    @Nullable
    @Setter
    private String url;

    @Nullable
    @Setter
    private List<String> linkedArticles;

    @Nullable
    private String imageId;

    @Setter
    private List<MenuResponseTableRow> children;

    public MenuResponseTableRow(
            String id, String name,
            int level, int position,
            String type, @Nullable String url,
            @Nullable String imageId
    ) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.position = position;
        this.type = type;
        this.url = url;
        this.imageId = imageId;
    }
}
