package ru.tpu.russian.back.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class NotificationRequestGroupDto extends NotificationBaseRequestDto {

    @ApiModelProperty(required = true, example = "ru", value = "Язык, определяющий группу рассылки")
    @NotNull
    private String language;

    @JsonIgnore
    private String topic;
}
