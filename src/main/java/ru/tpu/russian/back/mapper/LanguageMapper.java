package ru.tpu.russian.back.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.language.*;
import ru.tpu.russian.back.entity.language.*;
import ru.tpu.russian.back.service.MediaService;

@Component
public class LanguageMapper {

    private final MediaService mediaService;

    public LanguageMapper(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    public LanguageResponse convertToResponseForAndroid(Language language) {
        return new LanguageResponse(
                language.getId(),
                language.getShortName(),
                language.getName(),
                mediaService.getImage(language.getImageId())
        );
    }

    public LanguageResponseTableRow convertToResponseForTable(Language language, Long countUsers) {
        return new LanguageResponseTableRow(
                language.getId(),
                language.getShortName(),
                language.getName(),
                language.getImageId(),
                countUsers
        );
    }

    public LanguageBaseResponse convertToAvailableResponse(LanguageMapped language) {
        return new LanguageBaseResponse(
                language.getId(),
                language.getShortName(),
                language.getName()
        );
    }
}
