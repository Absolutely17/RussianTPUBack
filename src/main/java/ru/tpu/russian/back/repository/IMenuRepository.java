package ru.tpu.russian.back.repository;

import ru.tpu.russian.back.entity.Menu;

import java.util.*;

public interface IMenuRepository {

    List<Menu> getAll(Map<String, Object> params);
}
