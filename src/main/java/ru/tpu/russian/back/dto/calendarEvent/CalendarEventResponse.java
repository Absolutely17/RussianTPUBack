package ru.tpu.russian.back.dto.calendarEvent;

import lombok.*;
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

    private CalendarEventGroupTarget groupTarget;

}
