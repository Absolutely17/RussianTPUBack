package ru.tpu.russian.back.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class NotificationRequestGroupDto extends NotificationBaseRequestDto {

    @NotNull
    private String language;

    @JsonIgnore
    private String topic;
}
