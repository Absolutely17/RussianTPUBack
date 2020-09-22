package ru.tpu.russian.back.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import ru.tpu.russian.back.entity.User;

@Getter
@AllArgsConstructor
public class UserProfileResponse extends UserResponseDto {

    @ApiModelProperty(example = "true", value = "Подтверждена ли почта")
    private boolean isConfirmed;

    public UserProfileResponse(User user) {
        super(user);
        isConfirmed = user.isConfirm();
    }
}
