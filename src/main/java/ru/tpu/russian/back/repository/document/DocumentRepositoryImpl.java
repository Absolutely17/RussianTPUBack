package ru.tpu.russian.back.repository.document;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.entity.*;

import javax.persistence.*;
import java.util.List;

@Repository
public class DocumentRepositoryImpl implements IDocumentRepository {

    private static final String PROCEDURE_GET_DOCUMENT_WITHOUT_CONTENT = "GetDocumentsWithoutContent";

    private static final String PROCEDURE_GET_DOCUMENT_WITH_CONTENT = "GetDocumentsWithContent";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<DocumentWithoutContent> getDocumentWithoutContent(String email) {
        StoredProcedureQuery procedure = em.createNamedStoredProcedureQuery(PROCEDURE_GET_DOCUMENT_WITHOUT_CONTENT);
        procedure.setParameter("email", email);
        procedure.execute();
        return procedure.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentWithContent getDocumentWithContent(String id) {
        StoredProcedureQuery procedure = em.createNamedStoredProcedureQuery(PROCEDURE_GET_DOCUMENT_WITH_CONTENT);
        procedure.setParameter("documentId", id);
        procedure.execute();
        return (DocumentWithContent) procedure.getSingleResult();
    }
}
