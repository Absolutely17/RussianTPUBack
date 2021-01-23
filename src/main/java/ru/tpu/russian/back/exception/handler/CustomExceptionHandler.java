package ru.tpu.russian.back.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.exception.*;

import java.util.Locale;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Обработка исключений
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    private final MessageSource messageSource;

    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Обработка исключений на нарушение правил заполнения полей. Отсылаются для мобильного приложения.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessage> handleArgumentNotValidException(
            MethodArgumentNotValidException ex,
            Locale locale
    ) {
        log.error("ServerValidationError", ex);
        BindingResult result = ex.getBindingResult();
        String errorMessages = result.getAllErrors()
                .stream()
                .map(error -> {
                    try {
                        return messageSource.getMessage(error, locale);
                    } catch (NoSuchMessageException exception) {
                        return messageSource.getMessage(error, new Locale("en"));
                    }
                })
                .collect(joining("\n"));
        return new ResponseEntity<>(new ExceptionMessage(errorMessages, "ServerValidationError"), BAD_REQUEST);
    }

    /**
     * Обработка исключений по работе с веб-сервисом. Отсылаются мобильному приложению, имеют возможно локализации
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionMessage> handleBusinessException(
            BusinessException ex,
            Locale locale
    ) {
        log.error("BusinessError", ex);
        String errorMessage;
        try {
            errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        } catch (NoSuchMessageException exception) {
            errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), new Locale("en"));
        }
        return new ResponseEntity<>(new ExceptionMessage(errorMessage, ex.getType()), ex.getStatus());
    }

    /**
     * Обработк исключений неправильного заполнения полей для админ-панели
     */
    @ExceptionHandler(AttrValidationErrorException.class)
    public ResponseEntity<ExceptionMessage> handleAttrValidationException(
            AttrValidationErrorException ex
    ) {
        log.error("ServerValidationError", ex);
        return new ResponseEntity<>(
                new ExceptionMessage(ex.getMessage(), ex.getType(), ex.getErrors()), ex.getStatus());
    }

    /**
     * Обработка всех оставшихся исключений, откидываем общий текст и стектрейс
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleUncheckedInternalException(
            Exception ex,
            Locale locale
    ) {
        log.error("System error in service", ex);
        String errorMessage;
        try {
            errorMessage = messageSource.getMessage("Exception.service.internalProblem", null, locale);
        } catch (NoSuchMessageException exception) {
            errorMessage = messageSource.getMessage(
                    "Exception.service.internalProblem",
                    null,
                    new Locale("en")
            );
        }
        return new ResponseEntity<>(
                new ExceptionMessage(errorMessage, "SystemError", ExceptionUtils.getStackTrace(ex)),
                INTERNAL_SERVER_ERROR
        );
    }
}
