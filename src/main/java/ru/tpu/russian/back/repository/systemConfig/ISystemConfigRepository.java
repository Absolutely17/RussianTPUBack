package ru.tpu.russian.back.repository.systemConfig;

import ru.tpu.russian.back.dto.systemConfig.SystemParameterResponse;
import ru.tpu.russian.back.entity.SystemParameter;

import javax.annotation.Nullable;
import java.util.*;

public interface ISystemConfigRepository {

    /**
     * Вытащить дату начала учебы в текущем учебном году
     */
    @Nullable
    Date getStudyStartDate();

    /**
     * Вытащить все системные параметры
     */
    List<SystemParameter> getAllSystemParameter();

    void updateParameters(List<SystemParameterResponse> params);
}
