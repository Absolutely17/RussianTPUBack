package ru.tpu.russian.back.repository.dicts;

import ru.tpu.russian.back.entity.dict.*;

import java.util.List;

public interface IDictRepository {

    /**
     * Получить все учебные группы
     */
    List<StudyGroup> getStudyGroups();

    /**
     * Получить все языки, поддерживаемые в системе
     */
    List<Language> getAllLanguage();

    /**
     * Получить язык по его ID
     */
    Language getLanguageById(String id);
}
