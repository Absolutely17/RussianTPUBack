package ru.tpu.russian.back.repository.studyGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.dict.StudyGroup;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, String> {

    StudyGroup getById(String id);
}
