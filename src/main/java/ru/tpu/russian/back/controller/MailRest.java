package ru.tpu.russian.back.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.MailService;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
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

    @RequestMapping(method = GET, path = "/confirmation")
    public void confirmEmail(@RequestParam("token") String token, HttpServletResponse response) {
        mailService.confirmRegistration(token, response);
    }

    @RequestMapping(method = POST, path = "/send")
    public void reSendEmail(@RequestParam("email") String email) throws BusinessException {
        mailService.reSendEmail(email);
    }
}
