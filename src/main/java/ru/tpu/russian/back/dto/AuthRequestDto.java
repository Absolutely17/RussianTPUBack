package ru.tpu.russian.back.dto;

import lombok.*;
import org.springframework.lang.NonNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {

    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    private boolean rememberMe;
}
