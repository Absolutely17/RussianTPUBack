package ru.tpu.russian.back.dto.auth;

import lombok.*;

import javax.validation.constraints.*;

/**
 * Запрос аутентификации
 */
@Getter
@AllArgsConstructor
public class AuthRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|/\\\\]{8,}$")
    private String password;

    private boolean rememberMe;
}
