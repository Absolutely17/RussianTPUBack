package ru.tpu.russian.back.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
public class CheckAuthRequestDto {

    private String token;

    private String email;
}
