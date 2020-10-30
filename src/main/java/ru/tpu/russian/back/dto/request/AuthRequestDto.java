package ru.tpu.russian.back.dto.request;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
public class AuthRequestDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|/\\\\]{8,}$")
    private String password;

    private boolean rememberMe;
}
