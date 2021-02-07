package ru.tpu.russian.back.repository.document;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.document.DocumentUploadRequest;
import ru.tpu.russian.back.entity.document.*;

import javax.persistence.*;
import java.util.List;

@Repository
public class DocumentRepositoryImpl implements IDocumentRepository {

    private static final String GET_DOCUMENT_WITHOUT_CONTENT = "GetDocumentsWithoutContent";

    private static final String GET_DOCUMENT_WITH_CONTENT = "GetDocumentsWithContent";

    private static final String ATTACH_DOCUMENT_TO_USER = "AttachDocumentToUser";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<DocumentWithoutContent> getDocumentWithoutContent(String email) {
        return em.createNativeQuery("exec " + GET_DOCUMENT_WITHOUT_CONTENT + " :email", DocumentWithoutContent.class)
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
    public String uploadDocument(DocumentUploadRequest dto, byte[] document) {
        return (String)em.createNativeQuery("exec AddDocument :fileName, :documentName," +
                ":content, :adminEmail")
                .setParameter("fileName", dto.getFileName())
                .setParameter("documentName", dto.getDocumentName())
                .setParameter("content", document)
                .setParameter("adminEmail", dto.getAdminEmail())
                .getResultList().stream().findFirst().orElse(null);
    }

    @Transactional
    @Override
    public void attachDocumentToUser(String documentId, String userId) {
        em.createNativeQuery("exec " + ATTACH_DOCUMENT_TO_USER + " :documentId, :userId")
                .setParameter("documentId", documentId)
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
