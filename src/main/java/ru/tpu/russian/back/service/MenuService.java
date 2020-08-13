package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.response.MenuResponseDto;
import ru.tpu.russian.back.entity.Menu;
import ru.tpu.russian.back.repository.menu.MenuRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;

    private final MediaService mediaService;

    public MenuService(
            MenuRepository menuRepository,
            MediaService mediaService) {
        this.menuRepository = menuRepository;
        this.mediaService = mediaService;
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> getAll(String language) {
        log.info("Getting all menu items. Language = {}", language);
        Map<String, Object> params = new HashMap<>();
        params.put("Language", language);
        return convertToDto(menuRepository.getAll(params));
    }

    private List<MenuResponseDto> convertToDto(List<Menu> menuItems) {
        log.info("From DB received {} menu items", menuItems.size());
        menuItems.removeIf(menuItem -> menuItem.getLevel() != 1);
        return menuItems.stream()
                .map(this::convertToMenuResponse)
                .collect(Collectors.toList());
    }

    private MenuResponseDto convertToMenuResponse(Menu menuItem) {
        MenuResponseDto menuResponse = new MenuResponseDto(menuItem);
        if (menuItem.getImage() != null) {
            menuResponse.setImage(mediaService.getImage(menuItem.getImage()));
        }
        return menuResponse;
    }
}
