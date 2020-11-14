package ru.tpu.russian.back.repository.utils;

import javax.annotation.Nullable;
import java.util.Date;

public interface IUtilsRepository {

    /**
     * Вытащить дату начала учебы в текущем учебном году
     */
    @Nullable
    Date getStudyStartDate();
}
