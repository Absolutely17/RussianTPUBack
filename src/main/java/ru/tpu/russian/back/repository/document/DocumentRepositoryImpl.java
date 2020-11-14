package ru.tpu.russian.back.repository.document;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.request.DocumentUploadDto;
import ru.tpu.russian.back.entity.document.*;

import javax.persistence.*;
import java.util.List;

@Repository
public class DocumentRepositoryImpl implements IDocumentRepository {

    private static final String GET_DOCUMENT_WITHOUT_CONTENT = "GetDocumentsWithoutContent";

    private static final String GET_DOCUMENT_WITH_CONTENT = "GetDocumentsWithContent";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Document> getDocumentWithoutContent(String email) {
        return em.createNativeQuery("exec " + GET_DOCUMENT_WITHOUT_CONTENT + " :email", Document.class)
                .setParameter("email", email)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentWithContent getDocumentWithContent(String id) {
        return (DocumentWithContent) em.createNativeQuery("exec " + GET_DOCUMENT_WITH_CONTENT +
                " :documentId", DocumentWithContent.class)
                .setParameter("documentId", id)
                .getSingleResult();
    }

    @Override
    @Transactional
    public void uploadDocument(DocumentUploadDto dto, byte[] document) {
        em.createNativeQuery("exec AddDocument :fileName, :documentName," +
                ":content, :userEmail, :adminEmail")
                .setParameter("fileName", dto.getFileName())
                .setParameter("documentName", dto.getDocumentName())
                .setParameter("content", document)
                .setParameter("userEmail", dto.getUserEmail())
                .setParameter("adminEmail", dto.getAdminEmail())
                .executeUpdate();
    }
}
