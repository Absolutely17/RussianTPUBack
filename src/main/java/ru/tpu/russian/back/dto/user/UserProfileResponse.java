package ru.tpu.russian.back.dto.user;

import lombok.*;
import ru.tpu.russian.back.enums.UserGender;

@Getter
@AllArgsConstructor
public class UserProfileResponse extends UserResponse {

    private boolean isConfirmed;

    private String academicPlanUrl;

    private String scheduleUrl;

    public UserProfileResponse(
            String email,
            String firstName, String lastName,
            UserGender gender, String phoneNumber,
            String groupName, String languageId,
            String languageName, boolean isConfirmed,
            String academicPlanUrl, String scheduleUrl
    ) {
        super(email, firstName, lastName, gender, phoneNumber, groupName, languageId, languageName);
        this.isConfirmed = isConfirmed;
        this.academicPlanUrl = academicPlanUrl;
        this.scheduleUrl = scheduleUrl;
    }
}
