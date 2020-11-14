package ru.tpu.russian.back.dto.document;

import lombok.*;

/**
 * ДТО для отображения документов пользователю (без содержимого)
 */
@Getter
@AllArgsConstructor
public class DocumentResponse {

    private String name;

    private String loadDate;

    private String url;

    private String fileName;
}
