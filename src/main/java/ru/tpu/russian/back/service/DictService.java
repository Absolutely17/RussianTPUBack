package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.dict.StudyGroup;
import ru.tpu.russian.back.repository.dicts.IDictRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictService {

    private final IDictRepository dictRepository;

    public DictService(
            IDictRepository dictRepository
    ) {
        this.dictRepository = dictRepository;
    }

    public List<StudyGroup> getAllStudyGroups() {
        List<StudyGroup> groups = dictRepository.getStudyGroups();
        return groups.stream().sorted().collect(Collectors.toList());
    }
}
