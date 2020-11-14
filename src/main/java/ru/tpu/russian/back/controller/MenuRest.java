package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.SimpleNameObj;
import ru.tpu.russian.back.dto.menu.*;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.MenuService;

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

    @RequestMapping(method = GET)
    public List<MenuResponse> getMenu(
            @RequestParam String language,
            @RequestParam String email
    ) throws BusinessException {
        return menuService.getAll(language, email);
    }

    @RequestMapping(method = GET, path = "/dicts")
    public Map<String, List<SimpleNameObj>> getDicts() {
        return menuService.getDicts();
    }

    @RequestMapping(method = POST, path = "/save")
    public void save(@RequestBody MenuUpdateRequest request) {
        menuService.save(request);
    }
}
