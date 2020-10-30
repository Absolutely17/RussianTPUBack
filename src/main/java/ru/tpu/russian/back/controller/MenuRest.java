package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.response.MenuResponseDto;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.MenuService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/menu")
public class MenuRest {

    private final MenuService menuService;

    public MenuRest(MenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping(method = GET)
    public List<MenuResponseDto> getMenu(
            @RequestParam String language,
            @RequestParam String email
    ) throws BusinessException {
        return menuService.getAll(language, email);
    }
}
