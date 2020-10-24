package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.entity.dict.Language;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.repository.dicts.IDictRepository;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import static ru.tpu.russian.back.service.MailService.TypeMessages.CONFIRMATION_MESSAGE;
import static ru.tpu.russian.back.service.MailService.TypeMessages.RESET_PASSWORD_MESSAGE;

@Service
@Slf4j
public class MailService {

    protected enum TypeMessages {
        CONFIRMATION_MESSAGE,
        RESET_PASSWORD_MESSAGE
    }

    private static final String REDIRECT_URL = "https://tpu.ru/";

    private static final long EXPIRATION_CONFIRM_MAIL_TOKEN = 2L;

    private final JavaMailSender sender;

    private final JwtProvider jwtProvider;

    private final VelocityMerger merger;

    private final UserRepository userRepository;

    private final MessageSource messageSource;

    private final IDictRepository dictRepository;

    @Value("${spring.mail.from}")
    private String mailFrom;

    public MailService(
            JavaMailSender sender,
            JwtProvider provider,
            VelocityMerger merger,
            UserRepository userRepository,
            MessageSource messageSource,
            IDictRepository dictRepository
    ) {
        this.sender = sender;
        jwtProvider = provider;
        this.merger = merger;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.dictRepository = dictRepository;
    }

    public void confirmRegistration(String token, HttpServletResponse response) {
        log.debug("Check input token on valid.");
        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            int isSuccess = userRepository.editRegisteredStatus(email, true);
            log.info("Confirm email {} {}", email, isSuccess > 0 ? "success" : "failed");
        }
        try {
            response.sendRedirect(REDIRECT_URL);
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to redirect user to tpu.ru", ex);
        }
    }

    public void reSendConfirmationEmail(String email) throws BusinessException {
        User user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new BusinessException("Exception.login.user.notFound", email));
        if (!user.isConfirm()) {
            try {
                sendMessage(CONFIRMATION_MESSAGE, email, user.getLanguage());
            } catch (Exception ex) {
                log.error("Wrong in sending confirmation email.", ex);
                throw new BusinessException("Exception.mail.send");
            }
        } else {
            log.warn("User {} already confirmed.", email);
        }
    }

    public void sendMessage(TypeMessages type, String email, String languageId) {
        log.debug("Starting to create message type {}.", type.toString());
        Language lang = dictRepository.getLanguageById(languageId);
        Locale currentLocale;
        if (lang == null) {
            currentLocale = new Locale("en");
        } else {
            currentLocale = new Locale(lang.getShortName());
        }
        MimeMessage message;
        switch (type) {
            case CONFIRMATION_MESSAGE:
                message = createConfirmationMessage(currentLocale, email);
                break;
            case RESET_PASSWORD_MESSAGE:
                message = createResetPasswordMessage(currentLocale, email);
                break;
            default:
                throw new IllegalArgumentException("Wrong type message.");
        }
        sender.send(message);
    }

    private MimeMessage createResetPasswordMessage(Locale currentLocale, String email) {
        Map<String, Object> model = new LinkedHashMap<>();
        String token = jwtProvider.generateResetPasswordToken(email);
        model.put("token", token);
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(messageSource.getMessage("Mail.resetPassword.subject", null, currentLocale));
            helper.setFrom(mailFrom);
            helper.setText(merger.merge(model, currentLocale.toString(), RESET_PASSWORD_MESSAGE), true);
        } catch (Exception ex) {
            log.error("Error in creating confirmation message", ex);
        }
        return message;
    }

    private MimeMessage createConfirmationMessage(Locale currentLocale, String email) {
        Map<String, Object> model = new LinkedHashMap<>();
        String token = jwtProvider.generateTokenWithExpiration(email, EXPIRATION_CONFIRM_MAIL_TOKEN);
        model.put("email", email);
        model.put("token", token);
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(messageSource.getMessage("Mail.confirmation.subject", null, currentLocale));
            helper.setFrom(mailFrom);
            helper.setText(merger.merge(model, currentLocale.toString(), CONFIRMATION_MESSAGE), true);
        } catch (Exception ex) {
            log.error("Error in creating confirmation message", ex);
        }
        return message;
    }
}
