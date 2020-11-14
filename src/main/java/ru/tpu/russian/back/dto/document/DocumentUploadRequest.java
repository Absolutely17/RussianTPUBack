package ru.tpu.russian.back.dto.document;

import lombok.*;

/**
 * Загрузка документа пользователю
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUploadRequest {

    private String adminEmail;

    private String userEmail;

    /**
     * Название файла. Не изменяетя.
     */
    private String fileName;

    /**
     * То имя, которое будет отображено пользователю
     */
    private String documentName;
}
