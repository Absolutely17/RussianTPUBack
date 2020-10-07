package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.request.*;
import ru.tpu.russian.back.service.NotificationService;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path = "/api/notification")
public class NotificationRest {

    private final NotificationService notificationService;

    public NotificationRest(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ApiOperation(value = "Отправить уведомление на мобильное приложение опр. группе по языку")
    @RequestMapping(method = POST, path = "/group")
    public ResponseEntity<?> sendGroupNotification(
            @ApiParam(value = "Параметры уведомления для отправки", required = true)
            @RequestBody NotificationRequestGroupDto requestDto
    ) {
        return notificationService.sendOnGroup(requestDto);
    }

    @ApiOperation(value = "Отправить уведомление на мобильное приложение опр. пользоваетелям")
    @RequestMapping(method = POST, path = "/users")
    public ResponseEntity<?> sendNotificationOnUsers(
            @ApiParam(value = "Параметры уведомления для отправки", required = true)
            @RequestBody NotificationRequestUsersDto requestDto
    ) {
        return notificationService.sendOnUser(requestDto);
    }
}
