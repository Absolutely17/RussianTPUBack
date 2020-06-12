package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.Menu;
import ru.tpu.russian.back.repository.menu.MenuRepository;

import java.util.*;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> getAll(String language) {
        Map<String, Object> params = new HashMap<>();
        params.put("Language", language);
        List<Menu> menu = menuRepository.getAll(params);
        putChildrens(menu);
        return menu;
    }

    private void putChildrens(List<Menu> menu) {
        for (Menu m : menu) {
            String name = m.getName();
            for (Menu value : menu) {
                if (value.getParentName() != null && value.getParentName().equals(name)) {
                    m.getChildrens().add(value);
                }
            }
        }
        menu.removeIf(m -> m.getChildrens().isEmpty());
    }
}
