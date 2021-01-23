package ru.tpu.russian.back.exception;

import org.springframework.http.HttpStatus;

/**
 * Основной Exception приложения
 */
public abstract class BaseApplicationErrorException extends RuntimeException {

    /**
     * @param message сообщение исключения
     */
    public BaseApplicationErrorException(String message) {
        super(message);
    }

    /**
     * Статус исключения
     */
    public abstract HttpStatus getStatus();

    public abstract String getType();
}
