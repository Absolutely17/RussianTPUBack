package ru.tpu.russian.back.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.annotation.Nullable;
import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationBaseRequestDto {

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

    @ApiModelProperty(required = true, example = "new", value = "Тема сообщения, для объединения повторных уведомлений")
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
