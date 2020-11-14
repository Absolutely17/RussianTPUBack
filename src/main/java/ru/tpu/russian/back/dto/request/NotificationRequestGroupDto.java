package ru.tpu.russian.back.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import javax.validation.constraints.*;

@AllArgsConstructor
@Getter
@Setter
public class NotificationRequestGroupDto extends NotificationBaseRequestDto {

    @NotNull
    private String language;

    @JsonCreator
    public NotificationRequestGroupDto(
            @NotNull String title, @NotNull String message,
            @NotNull @Email String adminEmail, String topic,
            @NotNull String language
    ) {
        super(title, message, adminEmail, topic);
        this.language = language;
    }
}
