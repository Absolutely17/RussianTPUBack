package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.MenuResponseDto;
import ru.tpu.russian.back.dto.enums.MenuType;
import ru.tpu.russian.back.entity.Menu;
import ru.tpu.russian.back.repository.menu.MenuRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuResponseDto> getAll(String language) {
        Map<String, Object> params = new HashMap<>();
        params.put("Language", language);
        return convertToDto(menuRepository.getAll(params));
    }
    public List<MenuResponseDto> getMenuItemsStatic() {
        return testResponse();
    }
    private List<MenuResponseDto> testResponse() {
        List<MenuResponseDto> menu = new ArrayList<>();
        MenuResponseDto firstItem = new MenuResponseDto(UUID.randomUUID().toString(), "Учеба", 1, 1, MenuType.LINKS_LIST);
        List<MenuResponseDto> childsMenu = new ArrayList<>();
        childsMenu.add(new MenuResponseDto(UUID.randomUUID().toString(), "Расписание", 2, 1, MenuType.LINK, "google.com"));
        childsMenu.add(new MenuResponseDto(UUID.randomUUID().toString(), "Экзамены", 2, 2, MenuType.ARTICLE));
        firstItem.setChildrens(childsMenu);
        MenuResponseDto secondItem = new MenuResponseDto(UUID.randomUUID().toString(), "Общежитие", 1, 2, MenuType.FEED_LIST);
        menu.add(firstItem);
        menu.add(secondItem);
        return menu;
    }

    private List<MenuResponseDto> convertToDto(List<Menu> menuItems) {
        putChildrens(menuItems);
        return menuItems.stream()
                .map(MenuResponseDto::new)
                .collect(Collectors.toList());
    }

    private void putChildrens(List<Menu> menu) {
        for (Menu m : menu) {
            String idParent = m.getId();
            for (Menu value : menu) {
                if (value.getParentId() != null && value.getParentId().equals(idParent)) {
                    m.getChildrens().add(value);
                }
            }
        }
        menu.removeIf(m -> m.getChildrens().isEmpty());
    }
}
