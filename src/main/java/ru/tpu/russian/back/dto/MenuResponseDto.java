package ru.tpu.russian.back.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.dto.enums.MenuType;
import ru.tpu.russian.back.entity.Menu;

import java.util.*;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuResponseDto {

    private String id;

    private String name;

    private int level;

    private int position;

    private MenuType type;

    @Nullable
    private String url;

    private List<MenuResponseDto> childrens;

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
        childrens = menu.getChildrens()
                .stream()
                .map(MenuResponseDto::new)
                .collect(Collectors.toList());
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

    public List<MenuResponseDto> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<MenuResponseDto> childrens) {
        this.childrens = childrens;
    }

    public int getLevel() {
        return level;
    }

    @Nullable
    public String getUrl() {
        return url;
    }
}
