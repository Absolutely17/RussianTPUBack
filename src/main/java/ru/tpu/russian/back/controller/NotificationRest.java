package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.request.NotificationRequestDto;
import ru.tpu.russian.back.service.NotificationService;

import java.util.concurrent.ExecutionException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path = "/api/notification")
public class NotificationRest {

    private final NotificationService notificationService;

    public NotificationRest(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RequestMapping(method = POST)
    public void sendNotification(
            @RequestBody NotificationRequestDto requestDto
    ) throws ExecutionException, InterruptedException {
        notificationService.send(requestDto);
    }

}
