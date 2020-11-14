package ru.tpu.russian.back.dto.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.response.MenuResponseDto;
import ru.tpu.russian.back.entity.menu.Menu;

@Component
public class MenuMapper {

    public MenuResponseDto convertToResponse(Menu menu) {
        return new MenuResponseDto(
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
