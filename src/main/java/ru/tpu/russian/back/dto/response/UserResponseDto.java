package ru.tpu.russian.back.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    @ApiModelProperty(example = "test@test.com", value = "Email пользователя")
    private String email;

    @ApiModelProperty(example = "Ivan", value = "Имя пользователя")
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

    @ApiModelProperty(example = "88005553535", value = "Номер телефона пользователя")
    @Nullable
    private String phoneNumber;

    @ApiModelProperty(example = "8В7Б", value = "Номер учебной группы")
    @Nullable
    private String groupName;

    @ApiModelProperty(example = "uuid", value = "ID языка пользователя")
    private String languageId;

    @ApiModelProperty(example = "ru", value = "Наименование языка")
    private String languageName;

    public UserResponseDto(User user) {
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        middleName = user.getMiddleName();
        gender = user.getGender();
        languageId = user.getLanguage();
        phoneNumber = user.getPhoneNumber();
        groupName = user.getGroupName();
    }
}
