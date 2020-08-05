package ru.tpu.russian.back.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.dto.enums.ProviderType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDto {

    @ApiModelProperty(required = true, example = "qwerty123", value = "Пароль пользователя")
    private String password;

    @ApiModelProperty(required = true, example = "test@test.com", value = "Email пользователя")
    private String email;

    @ApiModelProperty(required = true, example = "Ivan", value = "Имя пользователя")
    private String firstName;

    @ApiModelProperty(example = "Ivanov", value = "Фамилия пользователя")
    @Nullable
    private String lastName;

    @ApiModelProperty(example = "Ivanovich", value = "Отчество пользователя")
    @Nullable
    private String middleName;

    @ApiModelProperty(example = "Male", value = "Пол пользователя")
    @Nullable
    private String gender;

    @ApiModelProperty(required = true, example = "Russian", value = "Язык пользователя")
    private String language;

    @ApiModelProperty(example = "88005553535", value = "Номер телефона")
    @Nullable
    private String phoneNumber;

    @ApiModelProperty(example = "google", value = "Сервис через который происходит аутентификация")
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
