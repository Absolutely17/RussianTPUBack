package ru.tpu.russian.back.repository.dicts;

import ru.tpu.russian.back.entity.dict.StudyGroup;

import java.util.List;

public interface IDictRepository {

    /**
     * Получить все учебные группы
     */
    List<StudyGroup> getStudyGroups();

}
