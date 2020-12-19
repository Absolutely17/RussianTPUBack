package ru.tpu.russian.back.dto.language;

import lombok.*;

/**
 * ДТО языка системы
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LanguageResponse extends LanguageBaseResponse {

    private byte[] image;

    public LanguageResponse(String id, String shortName, String fullName, byte[] image) {
        super(id, shortName, fullName);
        this.image = image;
    }
}
