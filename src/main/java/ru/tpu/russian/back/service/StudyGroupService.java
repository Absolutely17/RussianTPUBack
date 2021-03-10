package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.studyGroup.StudyGroupCreateRequest;
import ru.tpu.russian.back.entity.user.StudyGroup;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.repository.studyGroup.StudyGroupRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        group.setScheduleUrl(createDto.getScheduleUrl());
        group.setAcademicPlanUrl(createDto.getAcademicPlanUrl());
        studyGroupRepository.save(group);
    }

    public StudyGroup getById(String id) {
        return studyGroupRepository.getById(id);
    }

    public void editById(String id, StudyGroupCreateRequest editDto) throws BusinessException {
        StudyGroup group = studyGroupRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Данная группа отсутствует"));
        group.setName(editDto.getName());
        group.setScheduleUrl(editDto.getScheduleUrl());
        group.setAcademicPlanUrl(editDto.getAcademicPlanUrl());
        studyGroupRepository.save(group);
    }

    public void delete(String id) {
        log.info("Delete StudyGroup with ID = {}", id);
        studyGroupRepository.deleteById(id);
    }
}
