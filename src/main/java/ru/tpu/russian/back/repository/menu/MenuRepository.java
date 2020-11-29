package ru.tpu.russian.back.repository.menu;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.menu.*;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {

    List<Menu> getAllByLevelAndLanguageOrderByPosition(int level, String language);

    @Query("from MenuType where type = :type")
    MenuType getMenuType(@Param("type") String type);

    @Query("from MenuType")
    List<MenuType> getAllMenuType();
}
