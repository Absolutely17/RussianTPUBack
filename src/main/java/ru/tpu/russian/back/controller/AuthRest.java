package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.dto.request.*;
import ru.tpu.russian.back.dto.response.AuthResponseDto;
import ru.tpu.russian.back.entity.security.OAuthUserInfo;
import ru.tpu.russian.back.exception.InternalException;
import ru.tpu.russian.back.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
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
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Error due to incorrectly filled in field during registration (about 8 different errors)"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Email address {email} is already taken"
            )
    })
    @RequestMapping(method = POST, path = "/register")
    public void register(
            @ApiParam(value = "Данные пользователя для регистрации", required = true)
            @Valid @RequestBody BaseUserRequestDto registrationRequestDto
    ) throws InternalException {
        userService.register(registrationRequestDto);
    }

    @ApiOperation(value = "Аутентифицировать пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success",
                    response = AuthResponseDto.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "The specified password is invalid"
            ),
            @ApiResponse(
                    code = 400,
                    message = "The user {email} could not be found"
            )
    })
    @RequestMapping(method = POST, path = "/login")
    public AuthResponseDto login(
            @ApiParam(value = "Данные пользователя для аутентификации", required = true)
            @RequestBody AuthRequestDto authRequest
    ) throws InternalException {

        return userService.login(authRequest);
    }

    @ApiOperation(value = "Аутентификация через сторонние сервисы")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success",
                    response = AuthResponseDto.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "It looks like you are trying to log in from the wrong provider. " +
                            "Are you trying to log in through *PROVIDER*, " +
                            "but you need to enter through *PROVIDER IN DB*"
            ),
            @ApiResponse(
                    code = 401,
                    message = "This provider is not supported"
            ),
            @ApiResponse(
                    code = 210,
                    message = "You need to fill in the required data.",
                    response = OAuthUserInfo.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "The specified service is not supported for authentication"
            ),
            @ApiResponse(
                    code = 400,
                    message = "It looks like you are trying to authenticate with the wrong service. " +
                            "You are trying to login with {provider_in_request}, " +
                            "but you need to login with {provider_in_db}"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Problems with token from service"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Internal problems with the service. Try a little later"
            )
    })
    @RequestMapping(method = POST, path = "/login/provider")
    public ResponseEntity<?> loginWithService(
            @ApiParam(value = "Данные сервиса для аутентификации", required = true)
            @RequestBody AuthRequestWithServiceDto authServiceRequest
    ) throws InternalException {
        return userService.loginWithService(authServiceRequest);
    }

    @ApiOperation(value = "Регистрация нового пользователя, вошедшего с помощью соц. сети")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Error due to incorrectly filled in field during registration (about 8 different errors)"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Email address {email} is already taken"
            )
    })
    @RequestMapping(method = POST, path = "/register/provider")
    public void registerNewUserWithService(
            @ApiParam(value = "Данные для регистрации пользователя с помощью стороннего API", required = true)
            @Valid @RequestBody BaseUserRequestDto registrationRequest
    ) throws InternalException {
        userService.registerWithService(registrationRequest);
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
    @RequestMapping(method = POST, path = "/token/refresh")
    public AuthResponseDto refreshToken(
            HttpServletRequest servletRequest
    ) throws InternalException {
        return userService.refreshToken(servletRequest);
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
    @RequestMapping(method = POST, path = "/check")
    public ResponseEntity<?> checkAuth(
            @ApiParam(value = "Данные для проверки аутентификации пользователя", required = true)
            @RequestBody CheckAuthRequestDto requestDto
    ) {
        if (userService.checkAuth(requestDto)) {
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
