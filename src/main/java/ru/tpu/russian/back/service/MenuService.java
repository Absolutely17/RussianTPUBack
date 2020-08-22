package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.response.MenuResponseDto;
import ru.tpu.russian.back.entity.Menu;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.repository.menu.MenuRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;

    private final MediaService mediaService;

    @Value("${service.url}")
    private String serviceUrl;

    public MenuService(
            MenuRepository menuRepository,
            MediaService mediaService) {
        this.menuRepository = menuRepository;
        this.mediaService = mediaService;
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> getAll(String language) throws BusinessException {
        log.info("Getting all menu items. Language = {}", language);
        return convertToDto(menuRepository.getAll(language));
    }

    private List<MenuResponseDto> convertToDto(List<Menu> menuItems) throws BusinessException {
        if (menuItems.isEmpty()) {
            log.warn("Could not find menu items.");
            throw new BusinessException("Exception.menuItem.notFound");
        }
        log.info("From DB received {} menu items", menuItems.size());
        menuItems.removeIf(menuItem -> menuItem.getLevel() != 1);
        return menuItems.stream()
                .map(this::convertToMenuResponse)
                .collect(toList());
    }

    private MenuResponseDto convertToMenuResponse(Menu menuItem) {
        MenuResponseDto menuResponse = new MenuResponseDto(menuItem);
        List<Menu> children = menuItem.getChildren();
        if (!children.isEmpty()) {
            menuResponse.setChildren(children
                    .stream()
                    .map(this::convertToMenuResponse)
                    .collect(toList()));
        }
        if (menuItem.getImage() != null) {
            menuResponse.setImage(serviceUrl + "media/img/" + menuItem.getImage());
        }
        return menuResponse;
    }
}
