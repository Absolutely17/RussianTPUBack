package ru.tpu.russian.back.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.menu.*;
import ru.tpu.russian.back.entity.menu.Menu;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Component
public class MenuMapper {

    public MenuResponseAndroid convertToResponseForAndroid(Menu menu) {
        return new MenuResponseAndroid(
                menu.getName(),
                menu.getLevel(),
                menu.getPosition(),
                menu.getType().getType(),
                menu.getUrl(),
                menu.getArticle() != null ? menu.getArticle().getId() : null,
                menu.getImageId()
        );
    }

    public MenuResponseTableRow convertToResponseForTable(Menu menu) {
        MenuResponseTableRow response = new MenuResponseTableRow(
                menu.getId(),
                menu.getName(),
                menu.getLevel(),
                menu.getPosition(),
                menu.getType().getType(),
                menu.getUrl(),
                menu.getImageId()
        );
        if (menu.getArticle() != null) {
            response.setLinkedArticles(Collections.singletonList(menu.getArticle().getId()));
        } else if (menu.getLinkedArticles() != null) {
            response.setLinkedArticles(menu.getLinkedArticles()
                    .stream()
                    .map(it -> it.getArticle().getId())
                    .collect(toList()));
        }
        List<Menu> children = menu.getChildren();
        if (!children.isEmpty()) {
            response.setChildren(children
                    .stream()
                    .map(this::convertToResponseForTable)
                    .collect(toList()));
        }
        return response;
    }
}
