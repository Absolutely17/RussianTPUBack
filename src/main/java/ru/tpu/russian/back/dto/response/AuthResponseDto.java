package ru.tpu.russian.back.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponseDto {

    @ApiModelProperty(example = "token")
    private String token;

    @ApiModelProperty(example = "refresh token")
    private String refreshToken;

    @ApiModelProperty(example = "true", value = "Успешна ли аутентификация")
    private boolean success;

    @ApiModelProperty(value = "Данные аутентифирующегося пользователя")
    private UserResponseDto user;

    public AuthResponseDto(String token, boolean success, UserResponseDto user) {
        this.token = token;
        this.success = success;
        this.user = user;
    }

    public AuthResponseDto(String token, String refreshToken, boolean success) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.success = success;
    }
}
