package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.SimpleNameObj;
import ru.tpu.russian.back.dto.menu.*;
import ru.tpu.russian.back.entity.Article;
import ru.tpu.russian.back.entity.menu.*;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.mapper.MenuMapper;
import ru.tpu.russian.back.repository.article.ArticleRepository;
import ru.tpu.russian.back.repository.dicts.IDictRepository;
import ru.tpu.russian.back.repository.menu.MenuRepository;
import ru.tpu.russian.back.repository.systemConfig.ISystemConfigRepository;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Calendar.*;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class MenuService {

    private static final String SCHEDULE_TYPE = "SCHEDULE";

    private static final String SCHEDULE_URL_TPU = "https://rasp.tpu.ru";

    private static final String DUMMY_ID = "dummyId";

    private static final String MENU_TYPE_FEED_LIST = "FEED_LIST";

    public static final String MENU_TYPE_ARTICLE = "ARTICLE";

    private final MenuRepository menuRepository;

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    private final ISystemConfigRepository utilsRepository;

    private final MenuMapper menuMapper;

    private final IDictRepository dictRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${service.url}")
    private String serviceUrl;

    public MenuService(
            MenuRepository menuRepository,
            UserRepository userRepository,
            ISystemConfigRepository utilsRepository,
            MenuMapper menuMapper,
            IDictRepository dictRepository,
            ArticleRepository articleRepository
    ) {
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.utilsRepository = utilsRepository;
        this.menuMapper = menuMapper;
        this.dictRepository = dictRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuResponseAndroid> getAll(String language, String email) throws BusinessException {
        log.info("Getting all menu items. Language = {}", language);
        return convertToDto(menuRepository.getAllByLevelAndLanguageOrderByPosition(1, language), email);
    }

    private List<MenuResponseAndroid> convertToDto(List<Menu> menuItems, String email) throws BusinessException {
        if (menuItems.isEmpty()) {
            log.warn("Could not find menu items.");
            throw new BusinessException("Exception.menuItem.notFound");
        }
        log.info("From DB received {} menu items", menuItems.size());
        return menuItems.stream()
                .map(it -> convertToMenuResponse(it, email))
                .collect(toList());
    }

    private MenuResponseAndroid convertToMenuResponse(Menu menuItem, String email) {
        MenuResponseAndroid menuResponseAndroid = menuMapper.convertToResponseForAndroid(menuItem);
        List<Menu> children = menuItem.getChildren();
        if (!children.isEmpty()) {
            menuResponseAndroid.setChildren(children
                    .stream()
                    .map(it -> convertToMenuResponse(it, email))
                    .collect(toList()));
        }
        if (menuItem.getImageId() != null) {
            menuResponseAndroid.setImage(serviceUrl + "media/img/" + menuItem.getImageId());
        }
        if (SCHEDULE_TYPE.equals(menuItem.getType().getType()) && email != null) {
            menuResponseAndroid.setUrl(generateScheduleURL(email));
        }
        return menuResponseAndroid;
    }

    private String generateScheduleURL(String email) {
        Optional<String> userGroupId = userRepository.getGroupId(email);
        if (userGroupId.isPresent()) {
            Calendar cal = getInstance();
            Date studyStartDate = utilsRepository.getStudyStartDate();
            if (studyStartDate != null) {
                cal.setTime(studyStartDate);
            } else {
                return SCHEDULE_URL_TPU;
            }
            int weekStartStudy = cal.get(WEEK_OF_YEAR);
            cal.setTime(new Date());
            int weekCurrent = cal.get(WEEK_OF_YEAR);
            return SCHEDULE_URL_TPU + "/gruppa_" + userGroupId.get() + "/" + cal.get(YEAR) + "/"
                    + (weekCurrent - weekStartStudy + 1) + "/view.html";
        } else {
            return SCHEDULE_URL_TPU;
        }
    }

    @Transactional
    public List<MenuResponseTableRow> getMenuTable(String language) {
        List<Menu> menuFromDB = menuRepository.getAllByLevelAndLanguageOrderByPosition(1, language);
        return menuFromDB.stream()
                .map(menuMapper::convertToResponseForTable)
                .collect(toList());
    }

    public Map<String, List<SimpleNameObj>> getDicts() {
        Map<String, List<SimpleNameObj>> dicts = new HashMap<>();
        List<SimpleNameObj> languages = dictRepository.getAllLanguage()
                .stream()
                .map(it -> new SimpleNameObj(it.getId(), it.getFullName()))
                .collect(Collectors.toList());
        List<SimpleNameObj> types = menuRepository.getAllMenuType()
                .stream()
                .map(it -> new SimpleNameObj(it.getType(), it.getName()))
                .collect(toList());
        dicts.put("languages", languages);
        dicts.put("types", types);
        return dicts;
    }

    @Transactional
    public void saveOrUpdate(MenuUpdateRequest request) {
        Map<String, Menu> idNewItems = new HashMap<>();
        List<MenuItem> addedItems = request.getAddedItems();
        if (!addedItems.isEmpty()) {
            addedItems.stream()
                    .filter(it -> it.getParentId() == null || !it.getParentId().startsWith(DUMMY_ID))
                    .forEach(it -> {
                        idNewItems.put(it.getId(), addMenuItem(it, idNewItems));
                    });
            entityManager.flush();
            addedItems.stream()
                    .filter(it -> it.getParentId() != null && it.getParentId().startsWith(DUMMY_ID))
                    .forEach(it -> {
                        idNewItems.put(it.getId(), addMenuItem(it, idNewItems));
                    });
        }
        List<MenuItem> editedItems = request.getEditedItems();
        if (!editedItems.isEmpty()) {
            editedItems.forEach(it -> editMenuItem(it, idNewItems));
        }
        List<String> deletedItems = request.getDeletedItems();
        if (!deletedItems.isEmpty()) {
            deletedItems.forEach(it -> entityManager.remove(menuRepository.getOne(it)));
        }
    }

    /**
     * Добавляем новый пункт иеню
     *
     * @param dto        информация о пункте меню
     * @param idNewItems IDs новых добавленных
     * @return ID добавленного пункта
     */
    private Menu addMenuItem(MenuItem dto, Map<String, Menu> idNewItems) {
        Menu menu = new Menu();
        menu.setId(UUID.randomUUID().toString());
        editingMenuItem(dto, idNewItems, menu);
        menuRepository.save(menu);
        return menu;
    }

    /**
     * Меняем пункт меню
     *
     * @param dto        информация о пункте меню
     * @param idNewItems IDs новых добавленных пунктов
     */
    private void editMenuItem(MenuItem dto, Map<String, Menu> idNewItems) {
        Menu menu = menuRepository.getOne(dto.getId());
        menu.getLinkedArticles().clear();
        menu.setArticle(null);
        editingMenuItem(dto, idNewItems, menu);
        menuRepository.save(menu);
    }

    private void editingMenuItem(MenuItem dto, Map<String, Menu> idNewItems, Menu menu) {
        menu.setName(dto.getName());
        menu.setLevel(dto.getLevel());
        menu.setPosition(dto.getPosition());
        menu.setType(menuRepository.getMenuType(dto.getType()));
        menu.setLanguage(dto.getLanguageId());
        menu.setUrl(dto.getUrl());
        menu.setImageId(dto.getImage());
        List<String> linkedArticleIds = dto.getLinkedArticles();
        if (linkedArticleIds != null && !linkedArticleIds.isEmpty()) {
            if (MENU_TYPE_ARTICLE.equals(dto.getType()) && linkedArticleIds.size() == 1) {
                menu.setArticle(articleRepository.getById(linkedArticleIds.get(0)).orElse(null));
            } else if (MENU_TYPE_FEED_LIST.equals(dto.getType())) {
                List<Article> linkedArticles = entityManager.createNativeQuery("select * from ARTICLE" +
                        " where ID in (:ids)", Article.class)
                        .setParameter("ids", linkedArticleIds)
                        .getResultList();
                menu.getLinkedArticles().addAll(
                        linkedArticles.stream()
                                .map(it -> new MenuItemArticleLink(menu, it))
                                .collect(Collectors.toSet())
                );
            }
        }
        if (dto.getParentId() != null && dto.getParentId().startsWith(DUMMY_ID)) {
            menu.setParent(idNewItems.get(dto.getParentId()));
        } else if (dto.getParentId() != null) {
            menu.setParent(menuRepository.getOne(dto.getParentId()));
        } else {
            menu.setParent(null);
        }
    }
}
