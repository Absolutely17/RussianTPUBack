package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.SimpleNameObj;
import ru.tpu.russian.back.dto.mapper.MenuMapper;
import ru.tpu.russian.back.dto.request.*;
import ru.tpu.russian.back.dto.response.MenuResponseDto;
import ru.tpu.russian.back.entity.menu.Menu;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.repository.dicts.IDictRepository;
import ru.tpu.russian.back.repository.menu.MenuRepository;
import ru.tpu.russian.back.repository.user.UserRepository;
import ru.tpu.russian.back.repository.utils.IUtilsRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Calendar.*;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class MenuService {

    private static final String SCHEDULE_TYPE = "SCHEDULE";

    private static final String SCHEDULE_URL_TPU = "https://rasp.tpu.ru";

    private static final String DUMMY_ID = "dummyId";

    private final MenuRepository menuRepository;

    private final UserRepository userRepository;

    private final IUtilsRepository utilsRepository;

    private final MenuMapper menuMapper;

    private final IDictRepository dictRepository;

    @Value("${service.url}")
    private String serviceUrl;

    public MenuService(
            MenuRepository menuRepository,
            UserRepository userRepository,
            IUtilsRepository utilsRepository,
            MenuMapper menuMapper,
            IDictRepository dictRepository
    ) {
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.utilsRepository = utilsRepository;
        this.menuMapper = menuMapper;
        this.dictRepository = dictRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> getAll(String language, String email) throws BusinessException {
        log.info("Getting all menu items. Language = {}", language);
        return convertToDto(menuRepository.getAll(language), email);
    }

    private List<MenuResponseDto> convertToDto(List<Menu> menuItems, String email) throws BusinessException {
        if (menuItems.isEmpty()) {
            log.warn("Could not find menu items.");
            throw new BusinessException("Exception.menuItem.notFound");
        }
        log.info("From DB received {} menu items", menuItems.size());
        menuItems.removeIf(menuItem -> menuItem.getLevel() != 1);
        return menuItems.stream()
                .map(it -> convertToMenuResponse(it, email))
                .collect(toList());
    }

    private MenuResponseDto convertToMenuResponse(Menu menuItem, String email) {
        MenuResponseDto menuResponse = menuMapper.convertToResponse(menuItem);
        List<Menu> children = menuItem.getChildren();
        if (!children.isEmpty()) {
            menuResponse.setChildren(children
                    .stream()
                    .map(it -> convertToMenuResponse(it, email))
                    .collect(toList()));
        }
        if (menuItem.getImage() != null) {
            menuResponse.setImage(serviceUrl + "media/img/" + menuItem.getImage());
        }
        if (SCHEDULE_TYPE.equals(menuItem.getType().getType())) {
            menuResponse.setUrl(generateScheduleURL(email));
        }
        return menuResponse;
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

    public void save(MenuUpdateRequest request) {
        Map<String, String> idNewItems = new HashMap<>();
        List<MenuCreateDto> addedItems = request.getAddedItems();
        if (!addedItems.isEmpty()) {
            addedItems.stream()
                    .filter(it -> it.getParentId() == null || !it.getParentId().startsWith(DUMMY_ID))
                    .forEach(it -> {
                        idNewItems.put(it.getId(), addMenuItem(it, idNewItems));
                    });
            addedItems.stream()
                    .filter(it -> it.getParentId() != null && it.getParentId().startsWith(DUMMY_ID))
                    .forEach(it -> {
                        idNewItems.put(it.getId(), addMenuItem(it, idNewItems));
                    });
        }
        List<MenuCreateDto> editedItems = request.getEditedItems();
        if (!editedItems.isEmpty()) {
            editedItems.forEach(it -> editMenuItem(it, idNewItems));
        }
    }

    private String addMenuItem(MenuCreateDto dto, Map<String, String> idNewItems) {
        String id = randomUUID().toString();
        dto.setId(id);
        if (dto.getParentId() != null && dto.getParentId().startsWith(DUMMY_ID)) {
            dto.setParentId(idNewItems.get(dto.getParentId()));
        }
        menuRepository.saveItem(dto);
        return id;
    }

    private void editMenuItem(MenuCreateDto dto, Map<String, String> idNewItems) {
        if (dto.getParentId() != null && dto.getParentId().startsWith(DUMMY_ID)) {
            dto.setParentId(idNewItems.get(dto.getParentId()));
        }
        menuRepository.updateItem(dto);
    }
}
