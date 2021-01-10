package ru.tpu.russian.back.dto.user.calendarEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.tpu.russian.back.enums.CalendarEventGroupTarget;

/**
 * ДТО ответа на получение всех календарных событий пользователя
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventResponse {

    private String id;

    private String title;

    private String description;

    private String timestamp;

    private CalendarEventGroupTarget eventTarget;

}
