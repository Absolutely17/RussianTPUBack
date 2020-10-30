package ru.tpu.russian.back.dto.request;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@Setter
public class ResetPasswordDto {

    @NotNull
    private String token;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|/\\\\]{8,}$")
    private String password;
}
