package ru.tpu.russian.back.dto.user;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Pattern;

/**
 * ДТО для редактирования пользователя
 */
@Getter
@Setter
@AllArgsConstructor
public class UserProfileEditRequest extends BaseUserRequest {

    @Nullable
    @Setter
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|\\/\\\\]{8,}$")
    private String currentPassword;

    @Nullable
    @Setter
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|\\/\\\\]{8,}$")
    private String newPassword;

    @Override
    public String toString() {
        return super.toString();
    }
}
