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

    @ApiModelProperty(value = "Данные аутентифирующегося пользователя")
    private UserResponseDto user;

    public AuthResponseDto(String token, UserResponseDto user) {
        this.token = token;
        this.user = user;
    }

    public AuthResponseDto(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
