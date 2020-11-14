package ru.tpu.russian.back.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.tpu.russian.back.dto.user.UserResponse;

/**
 * Ответ на успешную аутентификацию
 */
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private String token;

    private String refreshToken;

    private UserResponse user;

    public AuthResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }

    public AuthResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
