package ru.tpu.russian.back.controller;

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

    @RequestMapping(method = POST, path = "/group")
    public ResponseEntity<?> sendGroupNotification(
            @RequestBody NotificationRequestGroupDto requestDto
    ) {
        return notificationService.sendOnGroup(requestDto);
    }

    @RequestMapping(method = POST, path = "/users")
    public ResponseEntity<?> sendNotificationOnUsers(
            @RequestBody NotificationRequestUsersDto requestDto
    ) {
        return notificationService.sendOnUser(requestDto);
    }
}
