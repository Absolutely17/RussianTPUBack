package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.request.AuthRequestDto;
import ru.tpu.russian.back.dto.response.AuthResponseDto;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static ru.tpu.russian.back.service.NotificationService.ROLE_ADMIN;

@Slf4j
@Service
public class TokenService {

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final UserService userService;

    public TokenService(JwtProvider jwtProvider, UserRepository userRepository, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public AuthResponseDto refreshToken(HttpServletRequest servletRequest) throws BusinessException {
        log.info("Refresh access token");
        String token = jwtProvider.getTokenFromRequest(servletRequest);
        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            String refreshSaltInToken = jwtProvider.getSaltFromRefreshToken(token);
            String refreshSaltInDB = userRepository.getUserByEmail(email)
                    .orElseThrow(() -> new BusinessException("Exception.login.user.notFound"))
                    .getRefreshSalt();
            if (refreshSaltInDB.equals(refreshSaltInToken)) {
                log.info("Salt matched. Generating new tokens and new salt. Email {}", email);
                return new AuthResponseDto(
                        jwtProvider.generateAccessToken(email),
                        jwtProvider.generateRefreshToken(email)
                );
            } else {
                log.error("The secret of the refresh token did not match.");
                throw new BusinessException("Exception.login.refreshToken.mismatchSalt");
            }
        } else {
            log.error("The token was not found in the request headers.");
            throw new BusinessException("Exception.login.token.notFoundOrInvalid");
        }
    }

    public boolean checkAuth(String token, String email) {
        log.info("Checking user on authenticated.");
        if (token != null && jwtProvider.validateToken(token)) {
            log.debug("Compare email in request with email in token.");
            String emailInToken = jwtProvider.getEmailFromToken(token);
            return email.equals(emailInToken);
        }
        log.warn("User not authenticated.");
        return false;
    }

    /**
     * Сгенерировать токен для администратора в веб-приложении для запросов на сервис
     */
    public ResponseEntity<?> generateTokenForAdmin(AuthRequestDto requestDto) throws BusinessException {
        User admin = userService.findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());
        if (ROLE_ADMIN.equals(admin.getRole())) {
            log.debug("Generate token for admin.");
            return new ResponseEntity<>(
                    jwtProvider.generateTokenWithExpiration(admin.getEmail(), 1L),
                    OK
            );
        }
        return new ResponseEntity<>(
                FORBIDDEN
        );
    }
}
