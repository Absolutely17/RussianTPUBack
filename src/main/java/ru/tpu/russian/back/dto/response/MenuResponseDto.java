package ru.tpu.russian.back.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.entity.Menu;
import ru.tpu.russian.back.enums.MenuType;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
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
    private String url;

    @Nullable
    @ApiModelProperty(value = "ID статьи, если TYPE = ARTICLE")
    private String idArticle;

    @Nullable
    @ApiModelProperty(value = "Изображение пункта меню")
    @Setter
    private byte[] image;

    @ApiModelProperty(value = "Дочерние пункты меню")
    @Setter
    @Nullable
    private List<MenuResponseDto> children;

    public MenuResponseDto(Menu menu) {
        id = menu.getId();
        name = menu.getName();
        level = menu.getLevel();
        position = menu.getPosition();
        type = menu.getType();
        url = menu.getUrl();
        idArticle = menu.getIdArticle();
        if (!menu.getChildren().isEmpty()) {
            children = menu.getChildren()
                    .stream()
                    .map(MenuResponseDto::new)
                    .collect(Collectors.toList());
        }
    }

}
