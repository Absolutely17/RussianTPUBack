package ru.tpu.russian.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.request.*;
import ru.tpu.russian.back.dto.response.AuthResponseDto;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.UserService;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/auth")
public class AuthRest {

    private UserService userService;

    public AuthRest(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = POST, path = "/local/registration")
    public void register(
            @Valid @RequestBody BaseUserRequestDto registrationRequestDto
    ) throws BusinessException {
        userService.register(registrationRequestDto);
    }


    @RequestMapping(method = POST, path = "/local/login")
    public AuthResponseDto login(
            @Valid @RequestBody AuthRequestDto authRequest
    ) throws BusinessException {

        return userService.login(authRequest);
    }


    @RequestMapping(method = POST, path = "/provider/login")
    public ResponseEntity<?> loginWithService(
            @RequestBody AuthRequestWithServiceDto authServiceRequest
    ) throws BusinessException {
        return userService.loginWithService(authServiceRequest);
    }


    @RequestMapping(method = POST, path = "/provider/registration")
    public void registerNewUserWithService(
            @Valid @RequestBody BaseUserRequestDto registrationRequest
    ) throws BusinessException {
        userService.registerWithService(registrationRequest);
    }

    @RequestMapping(method = POST, path = "/password/reset/request")
    public void resetPasswordRequest(
            @RequestParam("email") String email
    ) throws BusinessException {
        userService.resetPasswordRequest(email);
    }

    @RequestMapping(method = POST, path = "/password/reset")
    public void resetPassword(
            @Valid @RequestBody ResetPasswordDto resetDto
    ) throws BusinessException {
        userService.resetPassword(resetDto);
    }

    @RequestMapping(method = POST, path = "/fcmToken/save")
    public void saveFcmUserToken(
            @RequestBody NotificationTokenRequestDto requestDto
    ) {
        userService.saveFcmUserToken(requestDto);
    }

    @RequestMapping(method = POST, path = "/fcmToken/disable")
    public void disableFcmUserToken(
            @RequestParam("email") String email
    ) {
        userService.disableFcmUserToken(email);
    }

    @RequestMapping(method = POST, path = "/web-admin/login")
    public AuthResponseDto webLogin(@Valid @RequestBody AuthRequestDto requestDto) throws BusinessException {
        return userService.webLogin(requestDto);
    }
}
