package ru.tpu.russian.back.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.service.UserService;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static ru.tpu.russian.back.SpringFoxConfig.MAIL_CONFIRM_REST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/email")
@Api(tags = {MAIL_CONFIRM_REST})
public class MailConfirmRest {

    private final UserService userService;

    public MailConfirmRest(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = POST, path = "/confirm?token={token}")
    public void confirmEmail(@PathVariable String token) {
        userService.confirmRegistration(token);
    }
}
