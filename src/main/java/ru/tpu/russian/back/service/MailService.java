package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.exception.RegistrationException;
import ru.tpu.russian.back.jwt.JwtProvider;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
@Slf4j
public class MailService {

    private final JavaMailSender sender;

    private final JwtProvider jwtProvider;

    private final VelocityMerger merger;

    @Value("${spring.mail.from}")
    private String mailFrom;

    public MailService(
            JavaMailSender sender,
            JwtProvider provider,
            VelocityMerger merger
    ) {
        this.sender = sender;
        jwtProvider = provider;
        this.merger = merger;
    }

    public void sendMessage(String email, String firstName) throws RegistrationException {
        String token = jwtProvider.generateTokenForConfirmEmail(email);
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("token", token);
        model.put("name", firstName);
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("RussianTPU - Confirmation registration");
            helper.setFrom(mailFrom);
            helper.setText(merger.merge(model), true);
        } catch (MessagingException ex) {
            throw new RegistrationException(ex.getMessage());
        }
        log.debug("Sending message.");
        sender.send(message);
    }
}
