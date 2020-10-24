package ru.tpu.russian.back.repository.dicts;

import ru.tpu.russian.back.entity.dict.*;

import java.util.List;

public interface IDictRepository {

    List<StudyGroup> getStudyGroups();

    List<Language> getAllLanguage();

    Language getLanguageById(String id);
}
