package ru.tpu.russian.back.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.tpu.russian.back.Jwt.JwtProvider;
import ru.tpu.russian.back.dto.AuthResponseDto;
import ru.tpu.russian.back.entity.security.CustomUserDetails;
import ru.tpu.russian.back.repository.HttpCookieOAuth2AuthorizationRequestRepository;

import javax.servlet.http.*;
import java.io.*;
import java.util.Objects;

@Slf4j
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final JwtProvider jwtProvider;

    private final ObjectMapper objectMapper;

    public CustomSuccessHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                                JwtProvider jwtProvider,
                                ObjectMapper objectMapper) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.jwtProvider = jwtProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    @SneakyThrows
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String targetUrl = "/";
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(Objects.requireNonNull(createResponseWithTokens(user)));
        return UriComponentsBuilder.fromUriString(targetUrl)
                .build().toUriString();
    }

    private String createResponseWithTokens(CustomUserDetails user) throws JsonProcessingException {
        String token = jwtProvider.generateToken(user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
        return objectMapper.writeValueAsString(new AuthResponseDto(token, refreshToken, true));
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
