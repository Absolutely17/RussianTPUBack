package ru.tpu.russian.back.exception;

import lombok.*;

/**
 * ДТО ошибки проверки поля формы с фронта
 */
@AllArgsConstructor
@Getter
@Setter
public class AttrValidationError {

    /**
     * Название контрола на форме
     */
    private String name;

    /**
     * Текст ошибки
     */
    private String error;
}
