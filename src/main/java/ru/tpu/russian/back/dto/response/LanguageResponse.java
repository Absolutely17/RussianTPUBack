package ru.tpu.russian.back.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LanguageResponse {

    private String id;

    private String fullName;

    private String shortName;

    private byte[] image;
}
