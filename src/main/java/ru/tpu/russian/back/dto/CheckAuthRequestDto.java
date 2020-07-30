package ru.tpu.russian.back.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckAuthRequestDto {

    private String token;

    private String email;
}
