package ru.tpu.russian.back.repository.document;

import ru.tpu.russian.back.dto.document.DocumentUploadRequest;
import ru.tpu.russian.back.entity.document.*;

import java.util.List;

public interface IDocumentRepository {

    /**
     * Получить список документов для таблицы в моб. приложении, но без их контента
     */
    List<DocumentWithoutContent> getDocumentWithoutContent(String email);

    /**
     * Получить документ по ID вместе с его контентом
     */
    DocumentWithContent getDocumentWithContent(String id);

    /**
     * Загрузить документ
     */
    String uploadDocument(DocumentUploadRequest dto, byte[] document);

    /**
     * Прикрепить документ к пользователю
     */
    void attachDocumentToUser(String documentId, String userId);
}
