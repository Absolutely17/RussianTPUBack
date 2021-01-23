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
import ru.tpu.russian.back.repository.language.LanguageRepository;
import ru.tpu.russian.back.repository.menu.MenuRepository;
import ru.tpu.russian.back.repository.systemConfig.ISystemConfigRepository;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
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

    private final LanguageRepository languageRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${service.url}")
    private String serviceUrl;

    public MenuService(
            MenuRepository menuRepository,
            UserRepository userRepository,
            ISystemConfigRepository utilsRepository,
            MenuMapper menuMapper,
            LanguageRepository languageRepository,
            ArticleRepository articleRepository
    ) {
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.utilsRepository = utilsRepository;
        this.menuMapper = menuMapper;
        this.languageRepository = languageRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuResponseAndroid> getAll(String language, String email) throws BusinessException {
        log.debug("Getting all menu items. Language = {}", language);
        return mappingToDto(menuRepository.getAllByLevelAndLanguageOrderByPosition(1, language), email);
    }

    private List<MenuResponseAndroid> mappingToDto(List<Menu> menuItems, String email) throws BusinessException {
        if (menuItems.isEmpty()) {
            log.error("Could not find menu items.");
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
            if (studyStartDate == null) {
                return SCHEDULE_URL_TPU;
            }
            long currentWeek = calculateWeekBetweenDates(studyStartDate, cal) + 1;
            return SCHEDULE_URL_TPU + "/gruppa_" + userGroupId.get() + "/" + cal.get(YEAR) + "/"
                    + currentWeek + "/view.html";
        } else {
            return SCHEDULE_URL_TPU;
        }
    }

    private long calculateWeekBetweenDates(Date studyStartDate, Calendar calendar) {
        calendar.setTime(studyStartDate);
        int startWeek = calendar.get(WEEK_OF_YEAR);
        int startYear = calendar.get(YEAR);
        calendar.setTime(new Date());
        int currentWeek = calendar.get(WEEK_OF_YEAR);
        int currentYear = calendar.get(YEAR);
        if (currentYear == startYear) {
            return currentWeek - startWeek;
        } else {
            LocalDate date = LocalDate.of(startYear, 6, 1);
            long weeksInYear = IsoFields.WEEK_OF_WEEK_BASED_YEAR.rangeRefinedBy(date).getMaximum();
            return weeksInYear - startWeek + currentWeek;
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
        List<SimpleNameObj> languages = languageRepository.findAll()
                .stream()
                .map(it -> new SimpleNameObj(it.getId(), it.getName()))
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
