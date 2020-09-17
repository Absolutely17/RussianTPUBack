package ru.tpu.russian.back.repository.dicts;

import ru.tpu.russian.back.entity.StudyGroup;

import java.util.List;

public interface IDictsRepository {

    List<StudyGroup> getStudyGroups();
}
