package ru.tpu.russian.back.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@Setter
public class ResetPasswordDto {

    @ApiModelProperty(required = true, example = "token", value = "Токен для сброса пароля")
    @NotNull
    private String token;

    @ApiModelProperty(required = true, example = "qwerty123", value = "Новый пароль пользователя")
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|/\\\\]{8,}$")
    private String password;
}
