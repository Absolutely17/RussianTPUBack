package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.StudyGroup;
import ru.tpu.russian.back.repository.dicts.IDictsRepository;

import java.util.List;

@Service
public class DictsService {

    private final IDictsRepository dictsRepository;

    public DictsService(IDictsRepository dictsRepository) {
        this.dictsRepository = dictsRepository;
    }

    public List<StudyGroup> getAllStudyGroups() {
        return dictsRepository.getStudyGroups();
    }
}
