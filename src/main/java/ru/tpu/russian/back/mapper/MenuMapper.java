package ru.tpu.russian.back.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.menu.MenuResponse;
import ru.tpu.russian.back.entity.menu.Menu;

@Component
public class MenuMapper {

    public MenuResponse convertToResponse(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getLevel(),
                menu.getPosition(),
                menu.getType().getType(),
                menu.getUrl(),
                menu.getIdArticle()
        );
    }
}
