package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.MenuResponseDto;
import ru.tpu.russian.back.entity.Menu;
import ru.tpu.russian.back.service.MenuService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/menu", produces = APPLICATION_JSON_UTF8_VALUE)
public class MenuRest {

    private final MenuService menuService;

    public MenuRest(MenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping(method = GET, path = "")
    public List<MenuResponseDto> getMenu(@RequestParam String language) {
        return menuService.getAll(language);
    }

    @RequestMapping(method = GET, path = "/static")
    public List<MenuResponseDto> getStaticMenu() {
        return menuService.getMenuItemsStatic();
    }
}
