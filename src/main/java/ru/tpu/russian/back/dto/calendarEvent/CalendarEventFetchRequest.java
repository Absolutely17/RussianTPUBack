package ru.tpu.russian.back.dto.calendarEvent;

import lombok.*;

import javax.annotation.Nullable;

/**
 * Запрос на получение событий для: группы, пользователя или всех
 */
@NoArgsConstructor
@Getter
public class CalendarEventFetchRequest {

    @Nullable
    private String groupId;

    @Nullable
    private String userId;

    private boolean fetchAll;
}
