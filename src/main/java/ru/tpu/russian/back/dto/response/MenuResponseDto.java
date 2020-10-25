package ru.tpu.russian.back.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.enums.MenuType;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class MenuResponseDto {

    @ApiModelProperty(value = "ID пункта меню, генерируется БД")
    private String id;

    @ApiModelProperty(value = "Название пункта меню")
    private String name;

    @ApiModelProperty(value = "Уровень пункта меню")
    private int level;

    @ApiModelProperty(value = "Позиция пункта меню")
    private int position;

    @ApiModelProperty(value = "Тип пункта меню")
    private MenuType type;

    @Nullable
    @ApiModelProperty(value = "URL пункта меню, если TYPE = LINK")
    @Setter
    private String url;

    @Nullable
    @ApiModelProperty(value = "ID статьи, если TYPE = ARTICLE")
    private String idArticle;

    @Nullable
    @ApiModelProperty(value = "Изображение пункта меню")
    @Setter
    private String image;

    @ApiModelProperty(value = "Дочерние пункты меню")
    @Setter
    private List<MenuResponseDto> children;

    public MenuResponseDto(
            String id, String name,
            int level, int position,
            MenuType type, @Nullable String url,
            @Nullable String image
    ) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.position = position;
        this.type = type;
        this.url = url;
        this.image = image;
    }
}
