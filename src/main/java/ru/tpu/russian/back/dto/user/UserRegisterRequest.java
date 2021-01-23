package ru.tpu.russian.back.dto.user;

import lombok.*;

import javax.validation.constraints.*;

/**
 * ДТО для создания пользователя
 */
@Getter
@Setter
public class UserRegisterRequest extends BaseUserRequest {

    @NotNull
    @Setter
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|\\/\\\\]{8,}$")
    private String password;

    @Override
    public String toString() {
        return super.toString();
    }
}
