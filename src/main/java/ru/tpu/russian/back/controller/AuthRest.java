package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.dto.*;
import ru.tpu.russian.back.entity.security.OAuthUserInfo;
import ru.tpu.russian.back.exception.RegistrationException;
import ru.tpu.russian.back.service.UserService;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;
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
                    message = "Email address is not in the correct format."
            ),
            @ApiResponse(
                    code = 400,
                    message = "Email address already taken."
            )
    })
    @RequestMapping(method = POST, path = "/register")
    public ResponseEntity<?> register(
            @ApiParam(value = "Данные пользователя для регистрации", required = true)
            @RequestBody RegistrationRequestDto registrationRequestDto
    ) {
        try {
            userService.register(registrationRequestDto);
            return new ResponseEntity<>(
                    "Success", OK
            );
        } catch (RegistrationException ex) {
            return new ResponseEntity<>(
                    ex.getMessage(), BAD_REQUEST
            );
        }
    }

    @ApiOperation(value = "Аутентифицировать пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success",
                    response = AuthResponseDto.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "Password mismatch."
            ),
            @ApiResponse(
                    code = 401,
                    message = "User is not found."
            )
    })
    @RequestMapping(method = POST, path = "/login")
    public ResponseEntity<?> login(
            @ApiParam(value = "Данные пользователя для аутентификации", required = true)
            @RequestBody AuthRequestDto authRequest
    ) {
        try {
            AuthResponseDto response = userService.login(authRequest);
            return new ResponseEntity<>(
                    response, OK
            );
        } catch (LoginException ex) {
            return new ResponseEntity<>(
                    ex.getMessage(), UNAUTHORIZED
            );
        }
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
                            "Are you trying to log in through *PROVIDER*, but you need to enter through *PROVIDER IN DB*"
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
                    code = 401,
                    message = "Access token invalid."
            ),
            @ApiResponse(
                    code = 401,
                    message = "Problem with verifying token in Google API."
            ),
            @ApiResponse(
                    code = 401,
                    message = "Problem with Facebook API or with input data."
            ),
            @ApiResponse(
                    code = 401,
                    message = "UserID and Email must not be empty through VK auth"
            ),
            @ApiResponse(
                    code = 401,
                    message = "Problems with access to API VK or incorrect input data."
            ),
    })
    @RequestMapping(method = POST, path = "/login/provider")
    public ResponseEntity<?> loginWithService(
            @ApiParam(value = "Данные сервиса для аутентификации", required = true)
            @RequestBody AuthRequestWithServiceDto authServiceRequest
    ) {
        return userService.loginWithService(authServiceRequest);
    }

    @ApiOperation(value = "Регистрация нового пользователя, вошедшего с помощью соц. сети")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success"
            ),
            @ApiResponse(
                    code = 401,
                    message = "Minimum eight characters, at least one letter and one number."
            )
    })
    @RequestMapping(method = POST, path = "/register/provider")
    public ResponseEntity<?> registerNewUserWithService(
            @ApiParam(value = "Данные для регистрации пользователя с помощью стороннего API", required = true)
            @RequestBody RegistrationRequestDto registrationRequest
    ) {
        try {
            userService.registerWithService(registrationRequest);
            return new ResponseEntity<>(
                    "Success", OK
            );
        } catch (RegistrationException ex) {
            return new ResponseEntity<>(
                    ex.getMessage(), UNAUTHORIZED
            );
        }
    }

    @ApiOperation(value = "Обновление токена пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success",
                    response = AuthResponseDto.class
            ),
            @ApiResponse(
                    code = 401,
                    message = "The secret of the refresh token did not match."
            ),
            @ApiResponse(
                    code = 401,
                    message = "The token was not found in the request headers."
            )
    })
    @RequestMapping(method = POST, path = "/token/refresh")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest servletRequest
    ) {
        try {
            AuthResponseDto response = userService.refreshToken(servletRequest);
            return new ResponseEntity<>(
                    response, OK
            );
        } catch (LoginException ex) {
            return new ResponseEntity<>(
                    ex.getMessage(), OK
            );
        }
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
