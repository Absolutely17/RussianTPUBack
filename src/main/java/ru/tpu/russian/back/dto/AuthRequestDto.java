package ru.tpu.russian.back.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {

    @ApiModelProperty(required = true, example = "test@test.com", value = "Электронная почта пользователя")
    private String email;

    @ApiModelProperty(required = true, example = "qwerty123", value = "Пароль пользователя")
    private String password;

    @ApiModelProperty(example = "true", value = "Запомнить аутентификацию пользователя")
    private boolean rememberMe;
}
