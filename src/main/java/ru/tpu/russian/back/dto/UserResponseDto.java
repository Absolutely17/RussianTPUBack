package ru.tpu.russian.back.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import ru.tpu.russian.back.entity.User;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {

    @ApiModelProperty(example = "test@test.com", value = "Email пользователя")
    private String email;

    @ApiModelProperty(example = "Ivanov", value = "Фамилия пользователя")
    private String firstName;

    @ApiModelProperty(example = "Ivanovich", value = "Отчество пользователя")
    private String lastName;

    @ApiModelProperty(example = "Male", value = "Пол пользователя")
    private String gender;

    @ApiModelProperty(example = "Russian", value = "Язык пользователя")
    private String language;

    @ApiModelProperty(example = "88005553535", value = "Номер телефона пользователя")
    private String phoneNumber;

    public UserResponseDto(User user) {
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        gender = user.getGender();
        language = user.getLanguage();
        phoneNumber = user.getPhoneNumber();
    }
}
