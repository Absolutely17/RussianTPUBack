package ru.tpu.russian.back.repository.systemConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.systemConfig.SystemParameterResponse;
import ru.tpu.russian.back.entity.SystemParameter;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.text.*;
import java.util.*;

@Repository
@Slf4j
public class SystemConfigRepositoryImpl implements ISystemConfigRepository {

    private static final String GET_SYSTEM_PARAMETER = "GetSystemParameter";

    private static final String GET_ALL_PARAMETERS = "GetAllSystemParameters";

    private static final String UPDATE_PARAMETER = "UpdateSystemParameter";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Nullable
    @Transactional(readOnly = true)
    public Date getStudyStartDate() {
        String startDate = (String) em.createNativeQuery("exec " + GET_SYSTEM_PARAMETER + " :key")
                .setParameter("key", "studyStartDate")
                .getSingleResult();
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(startDate);
        } catch (ParseException ex) {
            log.error("Problem with value util parameter studyStartDate", ex);
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemParameter> getAllSystemParameter() {
        return em.createNativeQuery("exec " + GET_ALL_PARAMETERS, SystemParameter.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void updateParameters(List<SystemParameterResponse> params) {
        params.forEach(it -> {
            em.createNativeQuery("exec " + UPDATE_PARAMETER + " :id, :name, :key, :value, :description")
                    .setParameter("id", it.getId())
                    .setParameter("name", it.getName())
                    .setParameter("key", it.getKey())
                    .setParameter("value", it.getValue())
                    .setParameter("description", it.getDescription())
                    .executeUpdate();
        });
    }
}
