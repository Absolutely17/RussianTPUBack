package ru.tpu.russian.back.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.dto.enums.ProviderType;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDto {

    @ApiModelProperty(required = true, example = "qwerty123", value = "Пароль пользователя")
    @NotNull(message = "Password must be filled.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Minimum eight characters, " +
            "at least one letter and one number.")
    private String password;

    @ApiModelProperty(required = true, example = "test@test.com", value = "Email пользователя")
    @NotNull(message = "Email must be filled.")
    @Email(message = "This email address is not in the correct format.")
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
    @Size(min = 2, max = 20, message = "Middle name must be between 2 and 50 characters.")
    @NotNull(message = "Language must be filled.")
    private String language;

    @ApiModelProperty(example = "88005553535", value = "Номер телефона")
    @Nullable
    @Size(min = 5, max = 20, message = "Middle name must be between 5 and 50 characters.")
    @Pattern(regexp = "^\\d+$", message = "Phone number must contain only numbers")
    private String phoneNumber;

    @ApiModelProperty(example = "google", value = "Сервис через который происходит аутентификация")
    @Nullable
    private ProviderType provider = ProviderType.valueOf("local");

    @Override
    public String toString() {
        return "RegistrationRequestDto{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", gender='" + gender + '\'' +
                ", language='" + language + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", provider='" + provider + '\'' +
                '}';
    }
}
