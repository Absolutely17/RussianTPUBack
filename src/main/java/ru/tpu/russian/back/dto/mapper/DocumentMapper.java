package ru.tpu.russian.back.dto.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.response.DocumentResponse;
import ru.tpu.russian.back.entity.document.DocumentWithoutContent;

@Component
public class DocumentMapper {

    public DocumentResponse convertToResponseWithoutContent(DocumentWithoutContent document) {
        return new DocumentResponse(
                document.getName(),
                document.getLoadDate(),
                document.getFileName(),
                document.getUrl()
        );
    }
}
