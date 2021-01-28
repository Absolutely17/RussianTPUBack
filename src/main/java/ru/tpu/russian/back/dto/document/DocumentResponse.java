package ru.tpu.russian.back.dto.document;

import lombok.*;

/**
 * ДТО для отображения документов пользователю (без содержимого)
 */
@Getter
@AllArgsConstructor
public class DocumentResponse {

    private String id;

    private String name;

    private String fileName;

    private String url;

    private String lastUseDate;

    private String loadDate;
}
