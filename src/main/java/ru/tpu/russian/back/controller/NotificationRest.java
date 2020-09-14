package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.request.NotificationRequestDto;
import ru.tpu.russian.back.service.NotificationService;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path = "/api/notification")
public class NotificationRest {

    private final NotificationService notificationService;

    public NotificationRest(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ApiOperation(value = "Отправить уведомление на мобильное приложение")
    @RequestMapping(method = POST)
    public ResponseEntity<?> sendNotification(
            @ApiParam(value = "Параметры уведомления для отправки", required = true)
            @RequestBody NotificationRequestDto requestDto
    ) {
        return notificationService.send(requestDto);
    }

}
