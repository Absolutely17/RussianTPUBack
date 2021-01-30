package ru.tpu.russian.back.dto.user;

import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.enums.ProviderType;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserRequest {

    @NotNull
    @Email
    private String email;

    @Size(max = 50)
    @NotNull
    private String firstName;

    @Nullable
    @Size(max = 50)
    private String lastName;

    @Nullable
    @Size(max = 50)
    private String middleName;

    @Nullable
    @Pattern(regexp = "Male|Female")
    private String gender;

    @NotNull
    private String languageId;

    @Nullable
    @Pattern(regexp = "^[+]\\d+$")
    private String phoneNumber;

    private ProviderType provider = ProviderType.valueOf("local");

    @Nullable
    @Size(max = 10)
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
