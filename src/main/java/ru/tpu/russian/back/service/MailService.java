package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.User;
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
        log.info("Check input token on valid.");
        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            int isSuccess = userRepository.editRegisteredStatus(email, true);
            log.info("Confirm email {}", isSuccess > 0 ? "success" : "failed");
        }
    }

    public void reSendEmail(String email) throws MessagingException, IOException {
        User user = userRepository.getUserByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Wrong email or user does not exist."));
        sendMessage(user.getEmail(), user.getFirstName());
    }

    public void sendMessage(String email, String firstName) throws IOException, MessagingException {
        String token = jwtProvider.generateTokenForConfirmEmail(email);
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("token", token);
        model.put("name", firstName);
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("RussianTPU - Confirmation registration");
        helper.setFrom(mailFrom);
        helper.setText(merger.merge(model), true);
        sender.send(message);
    }
}
