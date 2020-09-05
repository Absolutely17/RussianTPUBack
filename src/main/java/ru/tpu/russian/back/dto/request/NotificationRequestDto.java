package ru.tpu.russian.back.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.tpu.russian.back.enums.Language;

@AllArgsConstructor
@Getter
@Setter
public class NotificationRequestDto {

    private Language language;

    private String title;

    private String message;

    @JsonIgnore
    private String topic;

    @Override
    public String toString() {
        return "NotificationRequestDto{" +
                "language=" + language +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
