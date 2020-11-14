package ru.tpu.russian.back.repository.media;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;

@Repository
public class MediaRepositoryImpl implements IMediaRepository {

    private static final String UPLOAD_IMAGE = "AddMedia";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public String upload(byte[] image) {
        return (String) em.createNativeQuery("exec " + UPLOAD_IMAGE +
                " :content")
                .setParameter("content", image)
                .getSingleResult();
    }
}
