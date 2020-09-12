package ru.tpu.russian.back.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
public class AuthRequestDto {

    @ApiModelProperty(required = true, example = "test@test.com", value = "Электронная почта пользователя")
    @NotNull
    @Email
    private String email;

    @ApiModelProperty(required = true, example = "qwerty123", value = "Пароль пользователя")
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|/\\\\]{8,}$")
    private String password;

    @ApiModelProperty(example = "true", value = "Запомнить аутентификацию пользователя")
    private boolean rememberMe;
}
