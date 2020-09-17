package ru.tpu.russian.back.repository.utils;

import javax.annotation.Nullable;
import java.util.Date;

public interface IUtilsRepository {

    @Nullable
    Date getStudyStartDate();
}
