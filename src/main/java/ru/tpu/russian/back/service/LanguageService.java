package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.language.*;
import ru.tpu.russian.back.mapper.LanguageMapper;
import ru.tpu.russian.back.repository.language.LanguageRepository;
import ru.tpu.russian.back.repository.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;

    private final UserRepository userRepository;

    private final LanguageMapper languageMapper;

    public LanguageService(
            LanguageRepository languageRepository,
            LanguageMapper languageMapper,
            UserRepository userRepository
    ) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
        this.userRepository = userRepository;
    }

    public List<LanguageResponse> getAllLanguage() {
        return languageRepository.findAll()
                .stream()
                .map(languageMapper::convertToResponseForAndroid)
                .collect(toList());
    }

    public void create(LanguageCreateRequest request) {
        languageRepository.addNewLanguage(request);
    }

    public List<LanguageResponseTableRow> getTable() {
        return languageRepository.findAll()
                .stream()
                .map(it -> languageMapper.convertToResponseForTable(it, userRepository.countByLanguage(it.getId())))
                .collect(toList());
    }

    public List<LanguageBaseResponse> getAllAvailableForCreate() {
        return languageRepository.getAllAvailable()
                .stream()
                .map(languageMapper::convertToAvailableResponse)
                .collect(Collectors.toList());
    }
}
