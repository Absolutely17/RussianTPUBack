package ru.tpu.russian.back.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
public class UserProfileResponse extends UserResponseDto {

    private boolean isConfirmed;

    public UserProfileResponse(
            String email,
            String firstName, String lastName,
            String middleName, String gender,
            String phoneNumber, String groupName,
            String languageId, String languageName,
            boolean isConfirmed
    ) {
        super(email, firstName, lastName, middleName, gender, phoneNumber, groupName, languageId, languageName);
        this.isConfirmed = isConfirmed;
    }
}
