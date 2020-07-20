package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.dto.*;
import ru.tpu.russian.back.exception.RegistrationException;
import ru.tpu.russian.back.service.UserService;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/auth")
@Api(tags = {SpringFoxConfig.AUTH_REST})
public class AuthRest {

    private UserService userService;

    public AuthRest(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Зарегистрировать пользователя")
    @RequestMapping(method = POST, path = "/register")
    public ResponseEntity register(
            @ApiParam(value = "Данные пользователя для регистрации", required = true)
            @RequestBody RegistrationRequestDto registrationRequestDto
    ) {
        try {
            userService.register(registrationRequestDto);
        } catch (RegistrationException ex) {
            return new ResponseEntity<>(
                    ex.getMessage(), BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                "Success", HttpStatus.OK
        );
    }

    @ApiOperation(value = "Аутентифицировать пользователя")
    @RequestMapping(method = POST, path = "/login")
    public AuthResponseDto login(
            @ApiParam(value = "Данные пользователя для аутентификации", required = true)
            @RequestBody AuthRequestDto authRequest
    ) {
        return userService.login(authRequest);
    }

    @ApiOperation(value = "Обновление токена пользователя")
    @RequestMapping(method = POST, path = "/token/refresh")
    public AuthResponseDto refreshToken(
            HttpServletRequest servletRequest
    ) {
        return userService.refreshToken(servletRequest);
    }
}
