package ru.tpu.russian.back.repository.document;

import ru.tpu.russian.back.entity.*;

import java.util.List;

public interface IDocumentRepository {

    List<DocumentWithoutContent> getDocumentWithoutContent(String email);

    DocumentWithContent getDocumentWithContent(String id);
}
