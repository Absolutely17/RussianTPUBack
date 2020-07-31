package ru.tpu.russian.back.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@AllArgsConstructor
public class AuthResponseDto {

    @ApiModelProperty(example = "token")
    private String token;

    @ApiModelProperty(example = "refresh token")
    private String refreshToken;

    @ApiModelProperty(example = "true", value = "Успешна ли аутентификация")
    private boolean success;

    @ApiModelProperty(value = "Данные аутентифирующегося пользователя")
    private UserResponseDto user;

    public AuthResponseDto(String token, boolean success) {
        this.token = token;
        this.success = success;
    }

    public AuthResponseDto(String token, String refreshToken, boolean success) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.success = success;
    }
}
