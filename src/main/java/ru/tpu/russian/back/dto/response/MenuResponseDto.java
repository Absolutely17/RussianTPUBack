package ru.tpu.russian.back.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class MenuResponseDto {

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

    @Setter
    private List<MenuResponseDto> children;

    public MenuResponseDto(
            String id,
            String name,
            int level,
            int position,
            String type,
            @Nullable String url,
            @Nullable String idArticle
    ) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.position = position;
        this.type = type;
        this.url = url;
        this.idArticle = idArticle;
    }
}
