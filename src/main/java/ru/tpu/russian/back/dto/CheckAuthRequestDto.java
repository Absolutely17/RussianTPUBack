package ru.tpu.russian.back.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class CheckAuthRequestDto {

    private String token;

    private String email;
}
