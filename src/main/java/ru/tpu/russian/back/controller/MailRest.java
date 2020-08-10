package ru.tpu.russian.back.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.service.MailService;

import javax.mail.MessagingException;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static ru.tpu.russian.back.SpringFoxConfig.MAIL_REST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/email")
@Api(tags = {MAIL_REST})
public class MailRest {

    private final MailService mailService;

    public MailRest(MailService mailService) {
        this.mailService = mailService;
    }

    @RequestMapping(method = POST, path = "/confirm?token={token}")
    public void confirmEmail(@PathVariable String token) {
        mailService.confirmRegistration(token);
    }

    @RequestMapping(method = POST, path = "/resend?email={email}")
    public void reSendEmail(@PathVariable String email) throws IOException, MessagingException {
        mailService.reSendEmail(email);
    }
}
