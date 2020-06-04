package ru.tpu.russian.back.controller;


import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.*;
import ru.tpu.russian.back.entity.*;
import ru.tpu.russian.back.service.*;


import java.util.*;

import static org.springframework.http.MediaType.*;
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
	public List<MenuView> getMenu(@RequestParam String language) {
		return menuService.getAll(language);
	}
}
