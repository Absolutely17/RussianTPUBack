package ru.tpu.russian.back.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private String patronymic;

    @ApiModelProperty(example = "Male", value = "Пол пользователя")
    @Nullable
    private String gender;

    @ApiModelProperty(required = true, example = "Russian", value = "Язык пользователя")
    private String language;

    @ApiModelProperty(example = "88005553535", value = "Номер телефона")
    @Nullable
    private String phoneNumber;

    @Override
    public String toString() {
        return "RegistrationRequestDto{" +
                "email='" + email + '\'' +
                ", firstname='" + firstName + '\'' +
                ", surname='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", sex=" + gender +
                ", language='" + language + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
