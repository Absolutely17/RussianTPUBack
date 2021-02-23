package ru.tpu.russian.back.dto.calendarEvent;

import lombok.*;
import ru.tpu.russian.back.enums.CalendarEventGroupTarget;

import java.util.List;

/**
 * ДТО создания события
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CalendarEventCreateRequest {

    private String title;

    private String description;

    private String timestamp;

    private CalendarEventGroupTarget groupTarget;

    private List<String> selectedUsers;

    private List<String> groups;

    private boolean sendNotification;

    private String detailedMessage;

    private String onlineMeetingLink;

    @Override
    public String toString() {
        return "CalendarEventCreateRequest{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", groupTarget=" + groupTarget +
                ", selectedUsers=" + selectedUsers +
                ", groups=" + groups +
                ", sendNotification=" + sendNotification +
                ", detailedMessage='" + detailedMessage + '\'' +
                ", onlineMeetingLink='" + onlineMeetingLink + '\'' +
                '}';
    }
}
