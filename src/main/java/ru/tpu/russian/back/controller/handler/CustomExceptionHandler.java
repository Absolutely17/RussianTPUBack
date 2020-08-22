package ru.tpu.russian.back.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.exception.*;

import java.util.Locale;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    private final MessageSource messageSource;

    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessage> handleArgumentNotValidException(
            MethodArgumentNotValidException ex,
            Locale locale
    ) {
        BindingResult result = ex.getBindingResult();
        String errorMessages = result.getAllErrors()
                .stream()
                .map(error -> messageSource.getMessage(error, locale))
                .collect(joining("\n"));
        return new ResponseEntity<>(new ExceptionMessage(errorMessages), BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionMessage> handleBusinessException(
            BusinessException ex,
            Locale locale
    ) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        return new ResponseEntity<>(new ExceptionMessage(errorMessage), BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleUncheckedInternalException(
            Exception ex,
            Locale locale
    ) {
        log.error("Internal error in service", ex);
        String errorMessage = messageSource.getMessage("Exception.service.internalProblem", null, locale);
        return new ResponseEntity<>(new ExceptionMessage(errorMessage), INTERNAL_SERVER_ERROR);
    }
}
