package ru.tpu.russian.back.repository.menu;

import ru.tpu.russian.back.dto.request.MenuCreateDto;
import ru.tpu.russian.back.entity.menu.*;

import java.util.List;

public interface IMenuRepository {

    /**
     * Получить все пункты меню
     */
    List<Menu> getAll(String language);

    /**
     * Получить все типы пункта меню, которые доступны в системе
     */
    List<MenuType> getAllMenuType();

    /**
     * Добавить новый пункт меню
     */
    void saveItem(MenuCreateDto dto);

    /**
     * Обновить пункт меню
     */
    void updateItem(MenuCreateDto dto);
}
