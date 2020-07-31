package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.dto.MenuResponseDto;
import ru.tpu.russian.back.service.MenuService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/menu")
@Api(tags = {SpringFoxConfig.MENU_REST})
public class MenuRest {

    private final MenuService menuService;

    public MenuRest(MenuService menuService) {
        this.menuService = menuService;
    }

    @ApiOperation(value = "Получение пунктов меню из БД")
    @RequestMapping(method = GET, path = "")
    public List<MenuResponseDto> getMenu(
            @ApiParam(value = "Язык пунктов меню", required = true)
            @RequestParam String language
    ) {
        return menuService.getAll(language);
    }
}
