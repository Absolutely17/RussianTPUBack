package ru.tpu.russian.back.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.calendarEvent.*;
import ru.tpu.russian.back.entity.calendarEvent.CalendarEvent;

import java.util.stream.Collectors;

/**
 * Маппер для объекта "Событие"
 */
@Component
public class CalendarEventMapper {

    public CalendarEvent convertToCalendarEventFromRequest(CalendarEventCreateRequest request) {
        return new CalendarEvent(
                request.getTitle(),
                request.getDescription(),
                request.getTimestamp(),
                request.getGroupTarget(),
                request.isSendNotification(),
                request.getOnlineMeetingLink()
        );
    }

    public CalendarEventResponse convertCalendarEventToResponse(CalendarEvent event) {
        return new CalendarEventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getTimestamp(),
                event.getTargetEnum()
        );
    }

    public CalendarEventDetailedResponse convertToDetailedResponse(CalendarEvent event) {
        return new CalendarEventDetailedResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getTimestamp(),
                event.getTargetEnum(),
                event.getOnlineMeetingLink(),
                event.getDetailedMessage() == null ? null : event.getDetailedMessage().getMessage()
        );
    }

    public CalendarEventDetailedResponseAdmin convertToDetailedResponseForAdmin(CalendarEvent event) {
        return new CalendarEventDetailedResponseAdmin(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getTimestamp(),
                event.getTargetEnum(),
                event.getOnlineMeetingLink(),
                event.getDetailedMessage() == null ? null : event.getDetailedMessage().getMessage(),
                event.getTargets().stream()
                        .filter(target -> target.getUser() != null)
                        .map(target -> target.getUser().getId())
                        .collect(Collectors.toList()),
                event.getTargets()
                        .stream()
                        .filter(target -> target.getGroup() != null)
                        .map(target -> target.getGroup().getId())
                        .collect(Collectors.toList()),
                event.isSendNotification()
        );
    }
}
