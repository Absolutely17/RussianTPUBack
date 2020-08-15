package ru.tpu.russian.back.controller;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.exception.*;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
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
        List<String> errorMessages = result.getAllErrors()
                .stream()
                .map(error -> messageSource.getMessage(error, locale))
                .collect(toList());
        return new ResponseEntity<>(new ExceptionMessage(errorMessages), BAD_REQUEST);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ExceptionMessage> handleRegistrationException(
            InternalException ex,
            Locale locale
    ) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        return new ResponseEntity<>(new ExceptionMessage(errorMessage), BAD_REQUEST);
    }
}
