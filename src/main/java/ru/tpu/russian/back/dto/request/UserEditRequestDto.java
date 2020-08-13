package ru.tpu.russian.back.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.enums.Languages;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEditRequestDto {

    @ApiModelProperty(required = true, example = "qwerty123", value = "Предыдущий пароль пользователя")
    @NotNull(message = "Previous password must be filled.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Minimum eight characters, " +
            "at least one letter and one number.")
    private String currentPassword;

    @ApiModelProperty(required = true, example = "qwerty123", value = "Новый пароль пользователя")
    @Nullable
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Minimum eight characters, " +
            "at least one letter and one number.")
    private String newPassword;

    @ApiModelProperty(required = true, example = "test@test.com", value = "Email пользователя")
    private String email;

    @ApiModelProperty(required = true, example = "Ivan", value = "Имя пользователя")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters.")
    @NotNull(message = "First name must be filled.")
    private String firstName;

    @ApiModelProperty(example = "Ivanov", value = "Фамилия пользователя")
    @Nullable
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters.")
    private String lastName;

    @ApiModelProperty(example = "Ivanovich", value = "Отчество пользователя")
    @Nullable
    @Size(min = 1, max = 50, message = "Middle name must be between 1 and 50 characters.")
    private String middleName;

    @ApiModelProperty(example = "Male", value = "Пол пользователя")
    @Nullable
    @Size(min = 1, max = 20, message = "Middle name must be between 1 and 50 characters.")
    private String gender;

    @ApiModelProperty(required = true, example = "Russian", value = "Язык пользователя")
    @NotNull(message = "Language must be filled.")
    private Languages language;

    @ApiModelProperty(example = "88005553535", value = "Номер телефона")
    @Nullable
    @Size(min = 5, max = 20, message = "Middle name must be between 5 and 50 characters.")
    @Pattern(regexp = "^\\d+$", message = "Phone number must contain only numbers")
    private String phoneNumber;
}
