package ru.tpu.russian.back.exception;

import lombok.Getter;

/**
 * Исключение, которое необходимо отобразить с сообщением, полученным исходя из языка пользователя
 */
@Getter
public class BusinessException extends Exception {

    private Object[] args;

    public BusinessException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public BusinessException(String message) {
        super(message);
    }
}
