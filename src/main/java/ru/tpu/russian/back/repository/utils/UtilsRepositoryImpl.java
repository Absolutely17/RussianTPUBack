package ru.tpu.russian.back.repository.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.text.*;
import java.util.Date;

@Repository
@Slf4j
public class UtilsRepositoryImpl implements IUtilsRepository {

    private static final String PROCEDURE_GET_UTIL_PARAMETER = "GetUtilParameter";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Nullable
    public Date getStudyStartDate() {
        String startDate = (String) em.createNativeQuery("exec " + PROCEDURE_GET_UTIL_PARAMETER + " :key")
                .setParameter("key", "studyStartDate")
                .getSingleResult();
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(startDate);
        } catch (ParseException ex) {
            log.error("Problem with value util parameter studyStartDate", ex);
            return null;
        }
    }
}
