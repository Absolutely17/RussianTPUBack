package ru.tpu.russian.back.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Исключение, которое необходимо отобразить с сообщением, полученным исходя из языка пользователя
 */
@Getter
public class BusinessException extends BaseApplicationErrorException {

    private final Object[] args;

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getType() {
        return "BusinessError";
    }

    /**
     * @param message сообщение исключения
     * @param args    дополнительные параметры, которые стоит вставить в сообщение
     */
    public BusinessException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    /**
     * @param message сообщение исключения
     */
    public BusinessException(String message) {
        super(message);
        args = new Object[0];
    }
}
