package ru.tpu.russian.back.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.enums.ProviderType;
import ru.tpu.russian.back.enums.UserGender;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserRequest {

    @NotNull
    @Setter
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|\\/\\\\]{8,}$")
    private String password;

    @Nullable
    @Setter
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!&^%$#@_|\\/\\\\]{8,}$")
    private String newPassword;

    @NotNull
    @Email
    private String email;

    @Size(min = 1, max = 50)
    @NotNull
    private String firstName;

    @Nullable
    @Size(min = 1, max = 50)
    private String lastName;

    @Nullable
    @Size(min = 1, max = 50)
    private String middleName;

    @Nullable
    @Pattern(regexp = "Male|Female")
    private UserGender gender;

    @NotNull
    private String languageId;

    @Nullable
    @Size(min = 5, max = 20)
    @Pattern(regexp = "^[+]\\d+$")
    private String phoneNumber;

    @Nullable
    private ProviderType provider = ProviderType.valueOf("local");

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
                ", language=" + languageId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", group='" + groupName + '\'' +
                '}';
    }
}
