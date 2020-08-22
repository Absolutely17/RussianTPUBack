package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.dto.response.AuthResponseDto;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.TokenService;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/token")
@Api(tags = {SpringFoxConfig.TOKEN_REST})
public class TokenRest {

    private final TokenService tokenService;

    public TokenRest(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @ApiOperation(value = "Обновление токена пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success",
                    response = AuthResponseDto.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "Failed to refresh token. The refresh token secret did not match"
            ),
            @ApiResponse(
                    code = 401,
                    message = "Token not found or damaged. Try to re-login"
            )
    })
    @RequestMapping(method = PUT)
    public AuthResponseDto refreshToken(
            HttpServletRequest servletRequest
    ) throws BusinessException {
        return tokenService.refreshToken(servletRequest);
    }

    @ApiOperation(value = "Проверка аутентификации пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Authenticated"
            ),
            @ApiResponse(
                    code = 401,
                    message = "Not authenticated"
            )
    })

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
                    "Not authenticated", UNAUTHORIZED
            );
        }
    }
}
