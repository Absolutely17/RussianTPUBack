package ru.tpu.russian.back.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
    private String adminEmail;

    @JsonIgnore
    private String topic = "news";

    @Override
    public String toString() {
        return "NotificationBaseRequestDto{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", email='" + adminEmail + '\'' +
                '}';
    }
}
