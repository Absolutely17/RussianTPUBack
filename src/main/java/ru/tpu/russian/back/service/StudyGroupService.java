package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.studyGroup.StudyGroupCreateRequest;
import ru.tpu.russian.back.entity.dict.StudyGroup;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.repository.studyGroup.StudyGroupRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;

    public StudyGroupService(
            StudyGroupRepository studyGroupRepository
    ) {
        this.studyGroupRepository = studyGroupRepository;
    }

    public List<StudyGroup> getAllStudyGroups() {
        return studyGroupRepository.findAll().stream().sorted().collect(Collectors.toList());
    }

    public void create(StudyGroupCreateRequest createDto) {
        StudyGroup group = new StudyGroup();
        group.setName(createDto.getName());
        group.setInternalID(createDto.getInternalID());
        studyGroupRepository.save(group);
    }

    public StudyGroup getById(String id) {
        return studyGroupRepository.getById(id);
    }

    public void editById(String id, StudyGroupCreateRequest editDto) throws BusinessException {
        StudyGroup group = studyGroupRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Данная группа отсутствует"));
        group.setName(editDto.getName());
        group.setInternalID(editDto.getInternalID());
        studyGroupRepository.save(group);
    }
}
