package ru.tpu.russian.back.repository.menu;

import ru.tpu.russian.back.entity.Menu;

import java.util.List;

public interface IMenuRepository {

    List<Menu> getAll(String language);
}
