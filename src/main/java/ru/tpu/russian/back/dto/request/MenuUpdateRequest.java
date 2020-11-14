package ru.tpu.russian.back.dto.request;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuUpdateRequest {

    private List<MenuCreateDto> addedItems;

    private List<MenuCreateDto> editedItems;
}
