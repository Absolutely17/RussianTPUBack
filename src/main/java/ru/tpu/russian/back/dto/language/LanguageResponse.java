package ru.tpu.russian.back.dto.language;

import lombok.*;

/**
 * ДТО языка системы
 */
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
