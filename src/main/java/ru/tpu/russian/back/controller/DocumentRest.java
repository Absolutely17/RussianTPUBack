package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tpu.russian.back.dto.document.*;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.DocumentService;

import javax.servlet.http.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/document")
public class DocumentRest {

    private final DocumentService documentService;

    public DocumentRest(DocumentService documentService) {
        this.documentService = documentService;
    }

    @RequestMapping(method = GET)
    public List<DocumentResponse> getDocumentsWithoutContent(
            @PathParam(value = "email") String email,
            HttpServletRequest request
    ) throws BusinessException {
        return documentService.getDocumentWithoutContent(email, request);
    }

    @RequestMapping(method = GET, path = "/download")
    public void getDocumentWithContent(
            @PathParam(value = "id") String id,
            HttpServletResponse response
    ) throws BusinessException {
        documentService.downloadDocument(id, response);
    }

    @RequestMapping(method = POST, path = "/upload", consumes = {"multipart/form-data"})
    public void uploadDocument(
            @RequestPart("documentInfo") DocumentUploadRequest dto,
            @RequestPart("document") MultipartFile doc
    ) throws IOException {
        documentService.uploadDocument(dto, doc);
    }
}
