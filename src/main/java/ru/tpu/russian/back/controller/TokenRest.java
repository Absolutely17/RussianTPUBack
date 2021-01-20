package ru.tpu.russian.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.auth.AuthResponse;
import ru.tpu.russian.back.exception.*;
import ru.tpu.russian.back.service.TokenService;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/token")
public class TokenRest {

    private final TokenService tokenService;

    public TokenRest(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Обновляем токены. Приходим если пользователь имеет refreshToken
     */
    @RequestMapping(method = PUT)
    public AuthResponse refreshToken(
            HttpServletRequest servletRequest
    ) throws BusinessException {
        return tokenService.refreshToken(servletRequest);
    }

    /**
     * Получить текущий статус аутентификации пользователя.
     */
    @RequestMapping(method = GET, path = "/status")
    public ResponseEntity<?> getStatusAuthUser(
            @RequestParam("token") String token,
            @RequestParam("email") String email
    ) {
        if (tokenService.checkAuth(token, email)) {
            return new ResponseEntity<>(
                    "Authenticated", OK
            );
        } else {
            return new ResponseEntity<>(
                    new ExceptionMessage("Unauthorized."),
                    UNAUTHORIZED
            );
        }
    }
}
