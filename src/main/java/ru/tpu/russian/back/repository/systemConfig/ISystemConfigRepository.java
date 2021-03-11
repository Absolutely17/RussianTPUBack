package ru.tpu.russian.back.repository.systemConfig;

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
     * Вытащить стандартную картинку пунктов меню
     */
    @Nullable
    String getDefaultMenuImage();

    /**
     * Вытащить все системные параметры
     */
    List<SystemParameter> getAllSystemParameter();
}
