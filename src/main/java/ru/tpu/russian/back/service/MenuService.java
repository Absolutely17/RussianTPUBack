package ru.tpu.russian.back.service;

import org.springframework.stereotype.*;
import ru.tpu.russian.back.dto.MenuResponseDto;
import ru.tpu.russian.back.entity.*;
import ru.tpu.russian.back.repository.*;

import java.util.*;

@Service
public class MenuService {

	private final MenuRepository menuRepository;

	public MenuService(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}

	public List<MenuView> getAll(String language) {
	    List<MenuView> menu = menuRepository.getAll(language);
        return menu;
	}
}
