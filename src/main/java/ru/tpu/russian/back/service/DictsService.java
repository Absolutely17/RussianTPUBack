package ru.tpu.russian.back.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.StudyGroup;
import ru.tpu.russian.back.repository.dicts.IDictsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictsService {

    private final IDictsRepository dictsRepository;

    public DictsService(IDictsRepository dictsRepository) {
        this.dictsRepository = dictsRepository;
    }

    @Cacheable(value = "study_groups")
    public List<StudyGroup> getAllStudyGroups() {
        List<StudyGroup> groups = dictsRepository.getStudyGroups();
        return groups.stream().sorted().collect(Collectors.toList());
    }
}
