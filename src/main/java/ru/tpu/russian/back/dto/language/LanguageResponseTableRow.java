package ru.tpu.russian.back.dto.language;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LanguageResponseTableRow extends LanguageBaseResponse {

    private String imageId;

    /**
     * Число пользователей с этим языком
     */
    private Long countUsers;

    public LanguageResponseTableRow(String id, String shortName, String fullName, String imageId, Long countUsers) {
        super(id, shortName, fullName);
        this.imageId = imageId;
        this.countUsers = countUsers;
    }
}
