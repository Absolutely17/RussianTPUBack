package ru.tpu.russian.back.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.joda.time.*;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionMessage {

    private String date;

    private String message;

    public ExceptionMessage(String message) {
        this.message = message;
        date = DateTime.now(DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss z");
    }
}
