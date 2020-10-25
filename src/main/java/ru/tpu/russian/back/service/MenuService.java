package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.mapper.MenuMapper;
import ru.tpu.russian.back.dto.response.MenuResponseDto;
import ru.tpu.russian.back.entity.Menu;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.repository.menu.MenuRepository;
import ru.tpu.russian.back.repository.user.UserRepository;
import ru.tpu.russian.back.repository.utils.IUtilsRepository;

import java.util.*;

import static java.util.Calendar.*;
import static java.util.stream.Collectors.toList;
import static ru.tpu.russian.back.enums.MenuType.SCHEDULE;

@Service
@Slf4j
public class MenuService {

    private static final String SCHEDULE_URL_TPU = "https://rasp.tpu.ru";

    private final MenuRepository menuRepository;

    private final UserRepository userRepository;

    private final IUtilsRepository utilsRepository;

    private final MenuMapper menuMapper;

    @Value("${service.url}")
    private String serviceUrl;

    public MenuService(
            MenuRepository menuRepository,
            UserRepository userRepository,
            IUtilsRepository utilsRepository,
            MenuMapper menuMapper
    ) {
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.utilsRepository = utilsRepository;
        this.menuMapper = menuMapper;
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
        if (menuItem.getType() == SCHEDULE) {
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
}
