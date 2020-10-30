package ru.tpu.russian.back.dto.request;

import lombok.*;

import javax.annotation.Nullable;
import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationBaseRequestDto {

    @NotNull
    private String title;

    @NotNull
    private String message;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String token;

    @Nullable
    private String topic = "news";

    @Override
    public String toString() {
        return "NotificationBaseRequestDto{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
