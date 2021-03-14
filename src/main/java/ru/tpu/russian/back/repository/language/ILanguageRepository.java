package ru.tpu.russian.back.repository.language;

import ru.tpu.russian.back.dto.language.LanguageCreateRequest;
import ru.tpu.russian.back.entity.language.LanguageAvailable;

import java.util.List;

public interface ILanguageRepository {

    List<LanguageAvailable> getAllAvailable();

    void addNewLanguage(LanguageCreateRequest request);

    void deleteLanguage(String id);
}
