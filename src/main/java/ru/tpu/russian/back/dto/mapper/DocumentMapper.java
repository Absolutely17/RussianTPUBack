package ru.tpu.russian.back.dto.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.response.DocumentResponse;
import ru.tpu.russian.back.entity.document.Document;

@Component
public class DocumentMapper {

    public DocumentResponse convertToResponseWithoutContent(Document document) {
        return new DocumentResponse(
                document.getName(),
                document.getLoadDate(),
                document.getUrl(),
                document.getFileName()
        );
    }
}
