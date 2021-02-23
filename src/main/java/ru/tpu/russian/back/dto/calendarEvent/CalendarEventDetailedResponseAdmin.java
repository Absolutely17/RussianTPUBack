package ru.tpu.russian.back.dto.calendarEvent;

import lombok.Getter;
import ru.tpu.russian.back.enums.CalendarEventGroupTarget;

import java.util.List;

/**
 * Детальная информация по событию для админки
 */
@Getter
public class CalendarEventDetailedResponseAdmin extends CalendarEventDetailedResponse {

    private List<String> selectedUsers;

    private List<String> selectedGroups;

    private boolean sendNotification;

    public CalendarEventDetailedResponseAdmin(
            String id, String title, String description, String timestamp,
            CalendarEventGroupTarget eventTarget,
            String onlineMeetingLink,
            String detailedMessage,
            List<String> selectedUsers,
            List<String> selectedGroups,
            boolean sendNotification
    ) {
        super(id, title, description, timestamp, eventTarget, onlineMeetingLink, detailedMessage);
        this.selectedUsers = selectedUsers;
        this.selectedGroups = selectedGroups;
        this.sendNotification = sendNotification;
    }
}
