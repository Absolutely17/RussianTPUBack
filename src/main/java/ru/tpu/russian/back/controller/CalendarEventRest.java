package ru.tpu.russian.back.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.calendarEvent.*;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.CalendarEventService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/calendarEvent")
public class CalendarEventRest {

    private final CalendarEventService calendarEventService;

    public CalendarEventRest(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    /**
     * Получить события в календаре для пользователя
     */
    @RequestMapping(method = GET)
    public List<CalendarEventResponse> getCalendarEventsForUser(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
    ) {
        return calendarEventService.getCalendarEvents(token);
    }

    /**
     * Получить событие по его ID
     */
    @RequestMapping(method = GET, path = "/{id}/detailed")
    public CalendarEventDetailedResponse getDetailedCalendarEvent(
            @PathVariable String id
    ) {
        return calendarEventService.getDetailedCalendarEvent(id);
    }

    /**
     * Получить таблицу событий для админки
     */
    @RequestMapping(method = POST, path = "/admin/events")
    public List<CalendarEventResponse> getEvents(
            @RequestBody CalendarEventFetchRequest request
    ) throws BusinessException {
        return calendarEventService.getEventsForAdmin(request);
    }

    /**
     * Создать событие (из админки только)
     */
    @RequestMapping(method = POST, path = "/admin/create")
    public void createEvent(
            @RequestBody CalendarEventCreateRequest request,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
    ) throws BusinessException {
        calendarEventService.createEvent(request, token);
    }

    /**
     * Получить событие по его ID
     */
    @RequestMapping(method = GET, path = "/admin/{id}/detailed")
    public CalendarEventDetailedResponse getDetailedCalendarEventForAdmin(
            @PathVariable String id
    ) {
        return calendarEventService.getDetailedCalendarEventForAdmin(id);
    }

    /**
     * Изменить событие
     */
    @RequestMapping(method = PUT, path = "/admin/{id}")
    public void editEvent(
            @PathVariable String id,
            @RequestBody CalendarEventCreateRequest request,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
    ) {
        calendarEventService.editEvent(id, request, token);
    }
}
