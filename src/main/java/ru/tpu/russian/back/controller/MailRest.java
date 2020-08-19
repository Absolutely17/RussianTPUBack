package ru.tpu.russian.back.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.exception.InternalException;
import ru.tpu.russian.back.service.MailService;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static ru.tpu.russian.back.SpringFoxConfig.MAIL_REST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/email")
@Api(tags = {MAIL_REST})
public class MailRest {

    private final MailService mailService;

    public MailRest(MailService mailService) {
        this.mailService = mailService;
    }

    @RequestMapping(method = PUT, path = "/confirmation?token={token}")
    public void confirmEmail(@PathVariable String token) {
        mailService.confirmRegistration(token);
    }

    @RequestMapping(method = POST, path = "/send?email={email}")
    public void reSendEmail(@PathVariable String email) throws InternalException {
        mailService.reSendEmail(email);
    }
}
