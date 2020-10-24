package ru.tpu.russian.back.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.response.LanguageResponse;
import ru.tpu.russian.back.entity.dict.*;
import ru.tpu.russian.back.repository.dicts.IDictRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DictService {

    private final IDictRepository dictRepository;

    private final MediaService mediaService;

    public DictService(
            IDictRepository dictRepository,
            MediaService mediaService
    ) {
        this.dictRepository = dictRepository;
        this.mediaService = mediaService;
    }

    @Cacheable(value = "study_groups")
    public List<StudyGroup> getAllStudyGroups() {
        List<StudyGroup> groups = dictRepository.getStudyGroups();
        return groups.stream().sorted().collect(Collectors.toList());
    }

    public List<LanguageResponse> getAllLanguage() {
        List<Language> languages = dictRepository.getAllLanguage();
        List<LanguageResponse> languageResponses = new ArrayList<>();
        for (Language lang : languages) {
            LanguageResponse langResponse = new LanguageResponse();
            langResponse.setId(lang.getId());
            langResponse.setFullName(lang.getFullName());
            langResponse.setShortName(lang.getShortName());
            langResponse.setImage(mediaService.getImage(lang.getImageId()));
            languageResponses.add(langResponse);
        }
        return languageResponses;
    }
}
