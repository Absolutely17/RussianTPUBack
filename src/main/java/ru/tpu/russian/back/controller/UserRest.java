package ru.tpu.russian.back.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.SimpleNameObj;
import ru.tpu.russian.back.dto.user.*;
import ru.tpu.russian.back.dto.user.calendarEvent.*;
import ru.tpu.russian.back.exception.*;
import ru.tpu.russian.back.service.UserService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/user")
public class UserRest {

    private final UserService userService;

    public UserRest(UserService userService) {
        this.userService = userService;
    }

    /**
     * Изменение настроек пользователя
     */
    @RequestMapping(method = PUT, path = "/edit")
    public void editUserInfo(
            @Valid @RequestBody UserProfileEditRequest requestDto
    ) throws BusinessException {
        userService.editUserProfile(requestDto);
    }

    /**
     * Получить текущий профиль пользователя
     */
    @RequestMapping(method = GET, path = "/profile")
    public UserProfileResponse getUserProfile(
            @PathParam(value = "email") String email
    ) throws BusinessException {
        return userService.getUserProfile(email);
    }

    /**
     * Получить события в календаре для пользователя
     */
    @RequestMapping(method = GET, path = "/calendarEvents")
    public List<CalendarEventResponse> getCalendarEventsForUser(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
    ) {
        return userService.getCalendarEvents(token);
    }

    /**
     * РЕСТы для админки
     */
    @RequestMapping(method = GET, path = "/admin/table")
    public List<UserTableRow> getUsersTable() {
        return userService.getUsersTable();
    }

    @RequestMapping(method = GET, path = "/admin/dicts")
    public Map<String, List<SimpleNameObj>> getDicts() {
        return userService.getDictsTable();
    }

    /**
     * Создаем событие в календаре пользователя
     */
    @RequestMapping(method = POST, path = "/admin/calendarEvent")
    public void createCalendarEvent(
            @RequestBody CalendarEventCreateRequest request,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token
    ) {
        userService.createCalendarEvent(request, token);
    }

    @RequestMapping(method = POST, path = "/admin")
    public void createUser(
            @RequestBody UserRegisterRequest request
    ) throws AttrValidationErrorException {
        userService.createUser(request);
    }

    @RequestMapping(method = PUT, path = "/admin/{id}")
    public void editUser(
            @PathVariable String id,
            @RequestBody UserProfileEditRequest request
    ) {
        userService.editUserFromAdminPanel(id, request);
    }

    /**
     * Удаляем пользователя
     */
    @RequestMapping(method = DELETE, path = "/admin/{id}")
    public void deleteUser(
            @PathVariable String id
    ) {
        userService.deleteUser(id);
    }
}
