package ru.tpu.russian.back.dto.request;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
public class NotificationTokenRequestDto {

    private String email;

    private String token;
}
