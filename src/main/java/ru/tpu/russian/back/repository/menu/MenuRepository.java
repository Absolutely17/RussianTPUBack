package ru.tpu.russian.back.repository.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.menu.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String>, IMenuRepository {

}
