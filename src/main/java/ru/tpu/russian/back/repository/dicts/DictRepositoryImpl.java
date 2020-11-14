package ru.tpu.russian.back.repository.dicts;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.entity.dict.*;

import javax.persistence.*;
import java.util.List;

@Repository
public class DictRepositoryImpl implements IDictRepository {

    private static final String GET_GROUPS = "GetStudyGroups";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<StudyGroup> getStudyGroups() {
        return em.createNativeQuery("exec " + GET_GROUPS, StudyGroup.class).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Language> getAllLanguage() {
        return em.createQuery("select main from Language main").getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Language getLanguageById(String id) {
        return em.find(Language.class, id);
    }
}
