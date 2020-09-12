package ru.tpu.russian.back.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import ru.tpu.russian.back.enums.Language;

import javax.validation.constraints.*;

@AllArgsConstructor
@Getter
@Setter
public class NotificationRequestDto {

    @ApiModelProperty(required = true, example = "ru", value = "Язык, определяющий группу рассылки")
    @NotNull
    private Language language;

    @ApiModelProperty(required = true, example = "Test title", value = "Заголовок уведомления")
    @NotNull
    private String title;

    @ApiModelProperty(required = true, example = "Test message", value = "Сообщение уведомления")
    @NotNull
    private String message;

    @ApiModelProperty(required = true, example = "test@test.com", value = "Электронная почта администратора")
    @NotNull
    @Email
    private String email;

    @ApiModelProperty(required = true, example = "token", value = "Токен администратора")
    @NotNull
    private String token;

    @JsonIgnore
    private String topic;

    @Override
    public String toString() {
        return "NotificationRequestDto{" +
                "language=" + language +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
