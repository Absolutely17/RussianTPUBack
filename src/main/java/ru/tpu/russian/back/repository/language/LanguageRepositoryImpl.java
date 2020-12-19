package ru.tpu.russian.back.repository.language;

import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.language.LanguageCreateRequest;
import ru.tpu.russian.back.entity.language.LanguageAvailable;

import javax.persistence.*;
import java.util.List;

public class LanguageRepositoryImpl implements ILanguageRepository {

    private static final String CREATE_NEW_LANGUAGE = "AddLanguage";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public List<LanguageAvailable> getAllAvailable() {
        return entityManager.createQuery("select main from LanguageAvailable main order by main.name")
                .getResultList();
    }

    @Transactional
    @Override
    public void addNewLanguage(LanguageCreateRequest request) {
        entityManager.createNativeQuery("exec " + CREATE_NEW_LANGUAGE + " :id, :imageId")
                .setParameter("id", request.getId())
                .setParameter("imageId", request.getImageId())
                .executeUpdate();
    }
}
