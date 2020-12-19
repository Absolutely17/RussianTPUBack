package ru.tpu.russian.back.dto.language;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LanguageBaseResponse {

    private String id;

    private String shortName;

    private String name;
}
