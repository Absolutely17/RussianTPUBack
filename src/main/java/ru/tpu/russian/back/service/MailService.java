package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.enums.Language;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Service
@Slf4j
public class MailService {

    private static final String REDIRECT_URL = "https://tpu.ru/";

    private final JavaMailSender sender;

    private final JwtProvider jwtProvider;

    private final VelocityMerger merger;

    private final UserRepository userRepository;

    private final MessageSource messageSource;

    @Value("${spring.mail.from}")
    private String mailFrom;

    public MailService(
            JavaMailSender sender,
            JwtProvider provider,
            VelocityMerger merger,
            UserRepository userRepository,
            MessageSource messageSource
    ) {
        this.sender = sender;
        jwtProvider = provider;
        this.merger = merger;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    public void confirmRegistration(String token, HttpServletResponse response) {
        log.debug("Check input token on valid.");
        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            int isSuccess = userRepository.editRegisteredStatus(email, true);
            log.info("Confirm email {}", isSuccess > 0 ? "success" : "failed");
        }
        try {
            response.sendRedirect(REDIRECT_URL);
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to redirect user to tpu.ru", ex);
        }
    }

    public void reSendEmail(String email) throws BusinessException {
        User user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new BusinessException("Exception.login.user.notFound", email));
        if (!user.isConfirm()) {
            try {
                sendMessage(user.getEmail(), user.getLanguage());
            } catch (Exception ex) {
                throw new BusinessException("Exception.mail.send");
            }
        } else {
            log.warn("User {} already confirmed.", email);
        }
    }

    public void sendMessage(String email, Language language) throws IOException, MessagingException {
        log.debug("Starting to create message.");
        String token = jwtProvider.generateTokenForConfirmEmail(email);
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("token", token);
        model.put("email", email);
        Locale currentLocale = new Locale(language.toString());
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(messageSource.getMessage("Mail.subject", null, currentLocale));
            helper.setFrom(mailFrom);
            helper.setText(merger.merge(model, language), true);
            log.info("Try to sending email to {}.", email);
            sender.send(message);
        } catch (Exception ex) {
            log.error("Wrong in sending message. Email {}", email, ex);
            throw ex;
        }
    }
}
