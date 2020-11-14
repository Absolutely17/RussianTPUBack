package ru.tpu.russian.back.repository.menu;

import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.menu.MenuItem;
import ru.tpu.russian.back.entity.menu.*;

import javax.persistence.*;
import java.util.List;

public class MenuRepositoryImpl implements IMenuRepository {

    private static final String GET_MENU = "GetMenuByLanguage";

    private static final String ADD_MENU_ITEM = "AddMenuItem";

    private static final String EDIT_MENU_ITEM = "EditMenuItem";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getAll(String language) {
        return em.createNativeQuery("exec " + GET_MENU + " :languageId", Menu.class)
                .setParameter("languageId", language)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuType> getAllMenuType() {
        return em.createNativeQuery("SELECT * FROM MENU_TYPE", MenuType.class).getResultList();
    }

    @Override
    @Transactional
    public void saveItem(MenuItem dto) {
        em.createNativeQuery("exec " + ADD_MENU_ITEM + " :id, :name," +
                " :level, :position, :languageId, :type, :parentId, :url, :articleId, :img")
                .setParameter("id", dto.getId())
                .setParameter("name", dto.getName())
                .setParameter("level", dto.getLevel())
                .setParameter("position", dto.getPosition())
                .setParameter("languageId", dto.getLanguageId())
                .setParameter("type", dto.getType())
                .setParameter("parentId", dto.getParentId())
                .setParameter("url", dto.getUrl())
                .setParameter("articleId", dto.getArticleId())
                .setParameter("img", dto.getImageId())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void updateItem(MenuItem dto) {
        em.createNativeQuery("exec " + EDIT_MENU_ITEM + " :id, :name," +
                " :level, :position, :type, :parentId, :url, :articleId, :img")
                .setParameter("id", dto.getId())
                .setParameter("name", dto.getName())
                .setParameter("level", dto.getLevel())
                .setParameter("position", dto.getPosition())
                .setParameter("parentId", dto.getParentId())
                .setParameter("type", dto.getType())
                .setParameter("url", dto.getUrl())
                .setParameter("articleId", dto.getArticleId())
                .setParameter("img", dto.getImageId())
                .executeUpdate();
    }
}
