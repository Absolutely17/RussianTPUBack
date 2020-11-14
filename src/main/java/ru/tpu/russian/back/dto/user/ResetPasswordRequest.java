package ru.tpu.russian.back.dto.user;

import lombok.*;

import javax.validation.constraints.*;

/**
 * Заявка на сброс пароля
 */
@Getter
@AllArgsConstructor
@Setter
public class ResetPasswordRequest {

    @NotNull
    private String token;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|/\\\\]{8,}$")
    private String password;
}
