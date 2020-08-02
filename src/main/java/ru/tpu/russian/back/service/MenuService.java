package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.response.MenuResponseDto;
import ru.tpu.russian.back.entity.Menu;
import ru.tpu.russian.back.repository.menu.MenuRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> getAll(String language) {
        log.info("Getting all menu items. Language = {}", language);
        Map<String, Object> params = new HashMap<>();
        params.put("Language", language);
        return convertToDto(menuRepository.getAll(params));
    }

    private List<MenuResponseDto> convertToDto(List<Menu> menuItems) {
        log.info("From DB received {} menu items", menuItems.size());
        menuItems.removeIf(menuItem -> menuItem.getLevel() != 1);
        return menuItems.stream()
                .map(MenuResponseDto::new)
                .collect(Collectors.toList());
    }
}
