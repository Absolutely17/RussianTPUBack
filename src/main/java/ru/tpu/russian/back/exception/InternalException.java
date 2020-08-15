package ru.tpu.russian.back.exception;

import lombok.Getter;

@Getter
public class InternalException extends Exception {

    private Object[] args;

    public InternalException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public InternalException(String message) {
        super(message);
    }
}
