package ru.tpu.russian.back.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Ошибка при проверке полей с админки
 */
@Getter
public class AttrValidationErrorException extends BaseApplicationErrorException {

    private final List<AttrValidationError> errors;

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getType() {
        return "ServerValidationError";
    }

    /**
     * Вывод листа ошибок при заполнении формы
     *
     * @param errors Лист ошибок.
     */
    public AttrValidationErrorException(List<AttrValidationError> errors) {
        super("Ошибка при проверке данных с формы");
        this.errors = errors;
    }
}
