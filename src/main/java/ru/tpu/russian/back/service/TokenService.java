package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.response.AuthResponseDto;
import ru.tpu.russian.back.exception.InternalException;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class TokenService {

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    public TokenService(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    public AuthResponseDto refreshToken(HttpServletRequest servletRequest) throws InternalException {
        log.info("Refresh access token");
        String token = jwtProvider.getTokenFromRequest(servletRequest);
        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            String refreshSaltInToken = jwtProvider.getSaltFromRefreshToken(token);
            String refreshSaltInDB = userRepository.getUserByEmail(email)
                    .orElseThrow(() -> new InternalException("Exception.login.notFound"))
                    .getRefreshSalt();
            if (refreshSaltInDB.equals(refreshSaltInToken)) {
                log.info("Salt matched. Generating new tokens and new salt. Email {}", email);
                return new AuthResponseDto(
                        jwtProvider.generateToken(email),
                        jwtProvider.generateRefreshToken(email)
                );
            } else {
                log.error("The secret of the refresh token did not match.");
                throw new InternalException("Exception.login.refreshToken.mismatch");
            }
        } else {
            log.error("The token was not found in the request headers.");
            throw new InternalException("Exception.login.token.notFoundOrInvalid");
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
}
