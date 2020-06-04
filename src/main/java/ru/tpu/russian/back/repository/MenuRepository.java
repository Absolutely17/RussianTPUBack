package ru.tpu.russian.back.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.query.*;
import ru.tpu.russian.back.entity.*;

import java.util.*;

public interface MenuRepository extends JpaRepository<MenuView, String>, JpaSpecificationExecutor {

	@Procedure("GetMenuByLanguage")
	List<MenuView> getAll(String language);
}
