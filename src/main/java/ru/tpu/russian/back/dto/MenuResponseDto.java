package ru.tpu.russian.back.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.dto.enums.MenuType;
import ru.tpu.russian.back.entity.Menu;

import java.util.*;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @ApiModelProperty(value = "Дочерние пункты меню")
    private List<MenuResponseDto> children;

    public MenuResponseDto(String id, String name, int level, int position, MenuType type) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.position = position;
        this.type = type;
    }

    public MenuResponseDto(String id, String name, int level, int position, MenuType type, String url) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.position = position;
        this.type = type;
        this.url = url;
    }

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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public MenuType getType() {
        return type;
    }

    public List<MenuResponseDto> getChildren() {
        return children;
    }

    public void setChildren(List<MenuResponseDto> children) {
        this.children = children;
    }

    public int getLevel() {
        return level;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getIdArticle() {
        return idArticle;
    }
}
