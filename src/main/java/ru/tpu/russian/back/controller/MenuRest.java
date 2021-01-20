package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.SimpleNameObj;
import ru.tpu.russian.back.dto.menu.*;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.MenuService;

import javax.annotation.Nullable;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/menu")
public class MenuRest {

    private final MenuService menuService;

    public MenuRest(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Получить все пункты меню для мобильного приложения
     */
    @RequestMapping(method = GET)
    public List<MenuResponseAndroid> getMenu(
            @RequestParam String language,
            @RequestParam @Nullable String email
    ) throws BusinessException {
        return menuService.getAll(language, email);
    }

    /**
     * РЕСТы для админки по обработке меню
     */
    @RequestMapping(method = GET, path = "/admin/table")
    public List<MenuResponseTableRow> getMenuTable(
            @RequestParam String language
    ) {
        return menuService.getMenuTable(language);
    }

    @RequestMapping(method = GET, path = "/admin/dicts")
    public Map<String, List<SimpleNameObj>> getDicts() {
        return menuService.getDicts();
    }

    @RequestMapping(method = POST, path = "/admin/save")
    public void save(@RequestBody MenuUpdateRequest request) {
        menuService.saveOrUpdate(request);
    }
}
