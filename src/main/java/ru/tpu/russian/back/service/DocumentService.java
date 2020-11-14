package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.tpu.russian.back.dto.document.*;
import ru.tpu.russian.back.entity.document.DocumentWithContent;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.mapper.DocumentMapper;
import ru.tpu.russian.back.repository.document.DocumentRepository;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

import static com.google.common.net.HttpHeaders.CONTENT_DISPOSITION;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class DocumentService {

    public static final String DOCUMENT_API_URL = "https://internationals.tpu.ru:8080/api/document/download?id=";

    private final DocumentRepository documentRepository;

    private final JwtProvider jwtProvider;

    private final DocumentMapper documentMapper;

    public DocumentService(
            DocumentRepository documentRepository,
            JwtProvider jwtProvider,
            DocumentMapper documentMapper
    ) {
        this.documentRepository = documentRepository;
        this.jwtProvider = jwtProvider;
        this.documentMapper = documentMapper;
    }

    public List<DocumentResponse> getDocumentWithoutContent(String email, HttpServletRequest request)
            throws BusinessException {
        log.info("Getting document by user {}", email);
        String token = jwtProvider.getTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)) {
            String emailInToken = jwtProvider.getEmailFromToken(token);
            if (email.equals(emailInToken)) {
                return documentRepository.getDocumentWithoutContent(email)
                        .stream()
                        .map(documentMapper::convertToResponseWithoutContent)
                        .collect(toList());
            } else {
                throw new BusinessException("Exception.login.token.notFoundOrInvalid");
            }
        } else {
            throw new BusinessException("Exception.login.token.notFoundOrInvalid");
        }
    }

    public void downloadDocument(String id, HttpServletResponse response) throws BusinessException {
        log.debug("Downloading document {}", id);
        DocumentWithContent document = documentRepository.getDocumentWithContent(id);
        if (document.getContent() != null) {
            response.setHeader(CONTENT_DISPOSITION, "attachment;filename=" + document.getFileName());
            try (ServletOutputStream outStream = response.getOutputStream()) {
                outStream.write(document.getContent());
                outStream.flush();
            } catch (IOException ex) {
                log.error("Error in downloading document {}", id, ex);
                throw new BusinessException("Exception.document.notFound");
            }
        } else {
            throw new BusinessException("Exception.document.notFound");
        }
    }

    public void uploadDocument(DocumentUploadRequest dto, MultipartFile doc) throws IOException {
        documentRepository.uploadDocument(dto, doc.getBytes());
    }
}
