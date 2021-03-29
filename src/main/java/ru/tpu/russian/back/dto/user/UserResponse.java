package ru.tpu.russian.back.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.enums.UserGender;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private String email;

    private String firstName;

    @Nullable
    private String lastName;

    @Nullable
    private UserGender gender;

    @Nullable
    private String phoneNumber;

    @Nullable
    private String groupName;

    private String languageId;

    private String languageName;

}
