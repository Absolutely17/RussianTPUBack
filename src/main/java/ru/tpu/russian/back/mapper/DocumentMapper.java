package ru.tpu.russian.back.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.document.DocumentResponse;
import ru.tpu.russian.back.entity.document.DocumentWithoutContent;

@Component
public class DocumentMapper {

    public DocumentResponse convertToResponseWithoutContent(DocumentWithoutContent document) {
        return new DocumentResponse(
                document.getName(),
                document.getLoadDate(),
                document.getUrl(),
                document.getFileName()
        );
    }
}
