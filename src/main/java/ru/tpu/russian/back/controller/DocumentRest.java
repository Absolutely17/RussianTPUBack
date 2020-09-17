package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.dto.response.DocumentResponse;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.DocumentService;

import javax.servlet.http.*;
import javax.websocket.server.PathParam;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/document")
@Api(tags = {SpringFoxConfig.DOCUMENT_REST})
public class DocumentRest {

    private final DocumentService documentService;

    public DocumentRest(DocumentService documentService) {
        this.documentService = documentService;
    }

    @ApiOperation(value = "Получение документов, привязанных к пользователю")
    @RequestMapping(method = GET)
    public List<DocumentResponse> getDocumentsWithoutContent(
            @ApiParam(value = "Электронная почта пользователя для которого достать документы нужно", required = true)
            @PathParam(value = "email") String email,
            HttpServletRequest request
    ) throws BusinessException {
        return documentService.getDocumentsWithoutContent(email, request);
    }

    @ApiOperation(value = "Скачать документ")
    @RequestMapping(method = GET, path = "/download")
    public void getDocumentWithContent(
            @ApiParam(value = "ID документа для скачивания", required = true)
            @PathParam(value = "id") String id,
            HttpServletResponse response
    ) throws BusinessException {
        documentService.downloadDocument(id, response);
    }
}
