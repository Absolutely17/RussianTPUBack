package ru.tpu.russian.back.dto;

import lombok.*;
import org.springframework.lang.NonNull;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private String token;

    private String refreshToken;

    @NonNull
    private boolean success;

    public AuthResponseDto(String token, boolean success) {
        this.token = token;
        this.success = success;
    }
}
