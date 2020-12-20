package ru.tpu.russian.back.dto.user;

import lombok.*;
import ru.tpu.russian.back.enums.CalendarEventGroupTarget;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CalendarEventCreateRequest {

    private String title;

    private String description;

    private String date;

    private CalendarEventGroupTarget groupTarget;

    private List<String> selectedUsers;

    private List<String> groups;

    private boolean sendNotification;
}
