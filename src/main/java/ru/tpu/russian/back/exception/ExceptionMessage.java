package ru.tpu.russian.back.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.joda.time.*;

import java.util.List;

/**
 * Общее ДТО исключения
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionMessage {

    private final String type;

    private final String date;

    private final String message;

    private List<AttrValidationError> errors;

    private String stacktrace;

    /**
     * @param message текст исключения
     */
    public ExceptionMessage(String message) {
        this.message = message;
        date = DateTime.now(DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss z");
        type = "BusinessError";
    }

    /**
     * @param message текст исключения
     * @param type    тип исключения
     */
    public ExceptionMessage(String message, String type) {
        this.message = message;
        date = DateTime.now(DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss z");
        this.type = type;
    }

    /**
     * @param message    текст исключения
     * @param type       тип исключения
     * @param stacktrace само исключения
     */
    public ExceptionMessage(String message, String type, String stacktrace) {
        this(message, type);
        this.stacktrace = stacktrace;
    }

    /**
     * @param message текст исключения
     * @param type    тип исключения
     * @param errors  список ошибок проверки данных с формы
     */
    public ExceptionMessage(String message, String type, List<AttrValidationError> errors) {
        this(message, type);
        this.errors = errors;
    }
}
