package ru.tpu.russian.back.dto.user.calendarEvent;

import lombok.Getter;
import ru.tpu.russian.back.enums.CalendarEventGroupTarget;

/**
 * ДТО детального представления события
 */
@Getter
public class CalendarEventDetailedResponse extends CalendarEventResponse {

    private String onlineMeetingLink;

    private String detailedMessage;

    public CalendarEventDetailedResponse(
            String id, String title, String description, String timestamp,
            CalendarEventGroupTarget eventTarget,
            String onlineMeetingLink,
            String detailedMessage
    ) {
        super(id, title, description, timestamp, eventTarget);
        this.detailedMessage = detailedMessage;
        this.onlineMeetingLink = onlineMeetingLink;
    }
}
