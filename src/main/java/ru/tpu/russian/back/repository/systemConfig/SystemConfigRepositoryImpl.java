package ru.tpu.russian.back.repository.systemConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.entity.SystemParameter;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.text.*;
import java.util.*;

@Slf4j
public class SystemConfigRepositoryImpl implements ISystemConfigRepository {

    private static final String GET_SYSTEM_PARAMETER = "GetSystemParameter";

    private static final String GET_ALL_PARAMETERS = "GetAllSystemParameters";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Nullable
    @Transactional(readOnly = true)
    public Date getStudyStartDate() {
        String startDate = (String)em.createNativeQuery("exec " + GET_SYSTEM_PARAMETER + " :key")
                .setParameter("key", "studyStartDate")
                .getResultList().stream().findFirst().orElse(null);
        if (startDate == null) {
            return null;
        }
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(startDate);
        } catch (ParseException ex) {
            log.error("Problem with value util parameter studyStartDate", ex);
            return null;
        }
    }

    @org.springframework.lang.Nullable
    @Override
    @Transactional(readOnly = true)
    public String getDefaultMenuImage() {
        return (String)em.createNativeQuery("exec " + GET_SYSTEM_PARAMETER + " :key")
                .setParameter("key", "defaultMenuItemImage")
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemParameter> getAllSystemParameter() {
        return em.createNativeQuery("exec " + GET_ALL_PARAMETERS, SystemParameter.class)
                .getResultList();
    }
}
