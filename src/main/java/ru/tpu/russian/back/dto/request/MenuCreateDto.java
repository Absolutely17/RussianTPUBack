package ru.tpu.russian.back.dto.request;

import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuCreateDto {

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
