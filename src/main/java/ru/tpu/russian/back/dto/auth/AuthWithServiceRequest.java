package ru.tpu.russian.back.dto.auth;

import lombok.*;
import org.springframework.lang.Nullable;

/**
 * Запрос аутентификации с помощью стороннего сервиса
 */
@AllArgsConstructor
@Getter
public class AuthWithServiceRequest {

    private String provider;

    private String token;

    /**
     * Эти данные приходят, если аутентификация через VK.
     * VK отдает email и user-id (нужен для запросов вместе с токеном).
     */
    @Nullable
    private Integer userId;

    @Nullable
    private String email;
}

