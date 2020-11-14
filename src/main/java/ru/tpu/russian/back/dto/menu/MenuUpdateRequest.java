package ru.tpu.russian.back.dto.menu;

import lombok.*;

import java.util.List;

/**
 * Запрос на обновление пунктов меню.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuUpdateRequest {

    private List<MenuItem> addedItems;

    private List<MenuItem> editedItems;
}
