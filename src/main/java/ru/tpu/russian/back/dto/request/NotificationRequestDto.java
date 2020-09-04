package ru.tpu.russian.back.dto.request;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class NotificationRequestDto {

    private String topic;

    private String title;

    private String message;

}
