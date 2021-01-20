package ru.tpu.russian.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.auth.*;
import ru.tpu.russian.back.dto.notification.NotificationTokenRequest;
import ru.tpu.russian.back.dto.user.*;
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

    /**
     * Локальная регистрация, путем ввода email и пароля
     */
    @RequestMapping(method = POST, path = "/local/registration")
    public void register(
            @Valid @RequestBody BaseUserRequest registrationRequestDto
    ) throws BusinessException {
        userService.register(registrationRequestDto);
    }

    /**
     * Вход по email и паролю
     */
    @RequestMapping(method = POST, path = "/local/login")
    public AuthResponse login(
            @Valid @RequestBody AuthRequest authRequest
    ) throws BusinessException {
        return userService.login(authRequest);
    }

    /**
     * Вход с использование сервисов
     */
    @RequestMapping(method = POST, path = "/provider/login")
    public ResponseEntity<?> loginWithService(
            @RequestBody AuthWithServiceRequest authServiceRequest
    ) throws BusinessException {
        return userService.loginWithService(authServiceRequest);
    }

    /**
     * Дорегистрация с использованием сервисов. Используется для дозаполнения недостающих полей.
     * Сюда приходим после предыдущего РЕСТа
     */
    @RequestMapping(method = POST, path = "/provider/registration")
    public void registerWithService(
            @Valid @RequestBody BaseUserRequest registrationRequest
    ) throws BusinessException {
        userService.registerWithService(registrationRequest);
    }

    /**
     * Посылает запрос на восстановление пароля
     */
    @RequestMapping(method = POST, path = "/password/reset/request")
    public void resetPasswordRequest(
            @RequestParam("email") String email
    ) throws BusinessException {
        userService.resetPasswordRequest(email);
    }

    /**
     * Восстанавливаем пароль (приходим из админки, там есть страница, куда приходит пользователь по ссылке из письма)
     */
    @RequestMapping(method = POST, path = "/password/reset")
    public void resetPassword(
            @Valid @RequestBody ResetPasswordRequest resetDto
    ) throws BusinessException {
        userService.resetPassword(resetDto);
    }

    /**
     * Сохраняем FCM токен пользователя. Нужен для персональной отправки уведомления.
     */
    @RequestMapping(method = POST, path = "/fcmToken/save")
    public void saveFcmUserToken(
            @RequestBody NotificationTokenRequest requestDto
    ) {
        userService.saveFcmUserToken(requestDto);
    }

    /**
     * Отключаем активность FCM токена. Дергаем РЕСТ при выходе пользователя в мобильном приложении.
     * Неактивному FCM токену уведомления не отправятся.
     */
    @RequestMapping(method = POST, path = "/fcmToken/disable")
    public void disableFcmUserToken(
            @RequestParam("email") String email
    ) {
        userService.disableFcmUserToken(email);
    }

    /**
     * РЕСТ для авторизация админа в админке
     */
    @RequestMapping(method = POST, path = "/admin/login")
    public AuthResponse webLogin(@Valid @RequestBody AuthRequest requestDto) throws BusinessException {
        return userService.webLogin(requestDto);
    }
}
