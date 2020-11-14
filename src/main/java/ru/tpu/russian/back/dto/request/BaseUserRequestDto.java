package ru.tpu.russian.back.dto.request;

import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.enums.ProviderType;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserRequestDto {

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
    @Size(min = 1, max = 20)
    private String gender;

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
