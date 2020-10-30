package ru.tpu.russian.back.dto.response;

import lombok.*;
import ru.tpu.russian.back.entity.User;

@Getter
@AllArgsConstructor
public class UserProfileResponse extends UserResponseDto {

    private boolean isConfirmed;

    public UserProfileResponse(User user) {
        super(user);
        isConfirmed = user.isConfirm();
    }
}
