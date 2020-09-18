package ru.tpu.russian.back.repository.dicts;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.entity.StudyGroup;

import javax.persistence.*;
import java.util.List;

@Repository
public class DictsRepositoryImpl implements IDictsRepository {

    private static final String PROCEDURE_GET_GROUPS = "GetStudyGroups";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<StudyGroup> getStudyGroups() {
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery(PROCEDURE_GET_GROUPS);
        storedProcedureQuery.execute();
        return storedProcedureQuery.getResultList();
    }
}
