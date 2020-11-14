package ru.tpu.russian.back.repository.document;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tpu.russian.back.entity.document.DocumentWithContent;

public interface DocumentRepository extends JpaRepository<DocumentWithContent, String>, IDocumentRepository {

}
