package ru.tpu.russian.back.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.*;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionMessage {

    private Date date;

    private String message;

    private List<String> messages;

    public ExceptionMessage(List<String> messages) {
        this.messages = messages;
        date = new Date();
    }

    public ExceptionMessage(String message) {
        this.message = message;
        date = new Date();
    }
}
