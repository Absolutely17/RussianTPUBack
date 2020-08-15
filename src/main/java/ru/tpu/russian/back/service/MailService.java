package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.exception.InternalException;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class MailService {

    private final JavaMailSender sender;

    private final JwtProvider jwtProvider;

    private final VelocityMerger merger;

    private final UserRepository userRepository;

    @Value("${spring.mail.from}")
    private String mailFrom;

    public MailService(
            JavaMailSender sender,
            JwtProvider provider,
            VelocityMerger merger,
            UserRepository userRepository
    ) {
        this.sender = sender;
        jwtProvider = provider;
        this.merger = merger;
        this.userRepository = userRepository;
    }

    public void confirmRegistration(String token) {
        log.debug("Check input token on valid.");
        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            int isSuccess = userRepository.editRegisteredStatus(email, true);
            log.info("Confirm email {}", isSuccess > 0 ? "success" : "failed");
        }
    }

    public void reSendEmail(String email) throws InternalException {
        Optional<User> user = userRepository.getUserByEmail(email);
        if (user.isPresent()) {
            try {
                sendMessage(user.get().getEmail(), user.get().getFirstName());
            } catch (Exception ex) {
                throw new InternalException("Exception.mail.send");
            }
        } else {
            log.error("Wrong email or user does not exist. Email {}", email);
            throw new InternalException("Exception.login.notFound", email);
        }
    }

    public void sendMessage(String email, String firstName) throws IOException, MessagingException {
        log.debug("Starting to create message.");
        String token = jwtProvider.generateTokenForConfirmEmail(email);
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("token", token);
        model.put("name", firstName);
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("RussianTPU - Confirmation registration");
            helper.setFrom(mailFrom);
            helper.setText(merger.merge(model), true);
            log.info("Try to sending email to {}.", email);
            sender.send(message);
        } catch (IOException | MessagingException ex) {
            log.error("Wrong in sending message. Email {}", email, ex);
            throw ex;
        }
    }
}
