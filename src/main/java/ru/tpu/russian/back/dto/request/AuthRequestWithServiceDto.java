package ru.tpu.russian.back.dto.request;

import lombok.*;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@Getter
public class AuthRequestWithServiceDto {

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

