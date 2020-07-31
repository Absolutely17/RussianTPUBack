package ru.tpu.russian.back.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@Getter
public class AuthRequestWithServiceDto {

    @ApiModelProperty(required = true, example = "google", value = "Сервис, через который проходит аутентификация")
    private String provider;

    @ApiModelProperty(required = true, example = "token", value = "Access token")
    private String token;

    /**
     * Эти данные приходят, если аутентификация через VK.
     * VK отдает email и user-id (нужен для запросов вместе с токеном).
     */
    @Nullable
    @ApiModelProperty(example = "14353461", value = "UserID при аутентификации через VK")
    private Integer userId;

    @Nullable
    @ApiModelProperty(example = "test@test.com", value = "Email при аутентификации через VK")
    private String email;
}

