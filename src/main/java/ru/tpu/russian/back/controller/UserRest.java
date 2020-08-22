package ru.tpu.russian.back.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.dto.request.BaseUserRequestDto;
import ru.tpu.russian.back.dto.response.UserResponseDto;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.UserService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/user")
@Api(tags = {SpringFoxConfig.USER_REST})
public class UserRest {

    private final UserService userService;

    public UserRest(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = PUT, path = "/edit")
    public void editUserInfo(@Valid @RequestBody BaseUserRequestDto requestDto) throws BusinessException {
        userService.editUser(requestDto);
    }

    @RequestMapping(method = GET, path = "/profile")
    public UserResponseDto getUserProfile(@PathParam(value = "email") String email) throws BusinessException {
        return userService.getUserProfile(email);
    }
}
