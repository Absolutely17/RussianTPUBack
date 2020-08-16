package ru.tpu.russian.back.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.tpu.russian.back.exception.ExceptionMessage;

import javax.servlet.http.*;
import java.io.*;
import java.util.Locale;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Autowired
    private MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        Locale locale = request.getLocale();
        boolean expired = request.getAttribute("expired") != null;
        String errorMessage = messageSource.getMessage(expired ? "Exception.token.expired" : "Exception.unauthorized",
                null, locale);
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        ExceptionMessage responseMessage = new ExceptionMessage(errorMessage);
        mapper.writeValue(out, responseMessage);
        out.flush();
    }
}
