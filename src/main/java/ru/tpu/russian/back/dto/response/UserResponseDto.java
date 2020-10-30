package ru.tpu.russian.back.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    private String email;

    private String firstName;

    @Nullable
    private String lastName;

    @Nullable
    private String middleName;

    @Nullable
    private String gender;

    @Nullable
    private String phoneNumber;

    @Nullable
    private String groupName;

    private String languageId;

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
