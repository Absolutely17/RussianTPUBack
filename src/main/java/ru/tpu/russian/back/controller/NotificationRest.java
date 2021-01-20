package ru.tpu.russian.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.notification.*;
import ru.tpu.russian.back.service.NotificationService;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path = "/api/notification")
public class NotificationRest {

    private final NotificationService notificationService;

    public NotificationRest(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Отправить уведомление группе по указанному параметру (может быть как язык, так и просто название группы отправки)
     */
    @RequestMapping(method = POST, path = "/admin/group")
    public ResponseEntity<?> sendGroupNotification(
            @RequestBody NotificationRequestGroup requestDto
    ) {
        return notificationService.sendOnGroup(requestDto);
    }

    /**
     * Отправить уведомление выбранным пользователям
     */
    @RequestMapping(method = POST, path = "/admin/users")
    public ResponseEntity<?> sendNotificationOnUsers(
            @RequestBody NotificationRequestUsers requestDto
    ) {
        return notificationService.sendOnUser(requestDto);
    }
}
