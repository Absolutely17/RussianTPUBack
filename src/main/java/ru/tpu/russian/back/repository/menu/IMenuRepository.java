package ru.tpu.russian.back.repository.menu;

import ru.tpu.russian.back.dto.menu.MenuItem;
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
    void saveItem(MenuItem dto);

    /**
     * Обновить пункт меню
     */
    void updateItem(MenuItem dto);
}
