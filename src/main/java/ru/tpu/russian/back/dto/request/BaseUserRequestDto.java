package ru.tpu.russian.back.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.enums.*;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserRequestDto {

    @ApiModelProperty(required = true, example = "qwerty123", value = "Пароль пользователя")
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|\\/\\\\]{8,}$")
    private String password;

    @ApiModelProperty(required = true, example = "qwerty123", value = "Новый пароль пользователя")
    @Nullable
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|\\/\\\\]{8,}$")
    private String newPassword;

    @ApiModelProperty(required = true, example = "test@test.com", value = "Email пользователя")
    @NotNull
    @Email
    private String email;

    @ApiModelProperty(required = true, example = "Ivan", value = "Имя пользователя")
    @Size(min = 1, max = 50)
    @NotNull
    private String firstName;

    @ApiModelProperty(example = "Ivanov", value = "Фамилия пользователя")
    @Nullable
    @Size(min = 1, max = 50)
    private String lastName;

    @ApiModelProperty(example = "Ivanovich", value = "Отчество пользователя")
    @Nullable
    @Size(min = 1, max = 50)
    private String middleName;

    @ApiModelProperty(example = "Male", value = "Пол пользователя")
    @Nullable
    @Size(min = 1, max = 20)
    private String gender;

    @ApiModelProperty(required = true, example = "Russian", value = "Язык пользователя")
    @NotNull
    private Language language;

    @ApiModelProperty(example = "88005553535", value = "Номер телефона")
    @Nullable
    @Size(min = 5, max = 20)
    @Pattern(regexp = "^[+]\\d+$")
    private String phoneNumber;

    @ApiModelProperty(example = "google", value = "Сервис через который происходит аутентификация")
    @Nullable
    private ProviderType provider = ProviderType.valueOf("local");

    @ApiModelProperty(example = "8В7Б", value = "Номер учебной группы")
    @Nullable
    @Size(min = 1, max = 10)
    private String groupName;

    @Override
    public String toString() {
        return "BaseUserRequestDto{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", gender='" + gender + '\'' +
                ", language=" + language +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", group='" + groupName + '\'' +
                '}';
    }
}
