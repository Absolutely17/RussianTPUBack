package ru.tpu.russian.back.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.dto.request.UserEditRequestDto;
import ru.tpu.russian.back.dto.response.UserResponseDto;
import ru.tpu.russian.back.service.UserService;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/user")
@Api(tags = {SpringFoxConfig.USER_REST})
public class UserRest {

    private final UserService userService;

    public UserRest(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = POST, path = "/edit")
    public void editUserInfo(@Valid @RequestBody UserEditRequestDto requestDto) throws LoginException {
        userService.editUser(requestDto);
    }

    @RequestMapping(method = GET, path = "/profile")
    public UserResponseDto getUserProfile(@PathParam(value = "email") String email) {
        return userService.getUserProfile(email);
    }

//    @RequestMapping(method = GET, path = "/language")
//    public String getUsersByLanguage(@RequestParam("language") String language) {
//        StringBuilder sb = new StringBuilder();
//        for (User s : userService.getAllByLanguage(language)) {
//            sb.append(s.toString()).append("\n");
//        }
//        return sb.toString();
//    }
//
//    @RequestMapping(method = GET, path = "/reg")
//    public String getUsersByReg(@RequestParam("reg") boolean reg) {
//        StringBuilder sb = new StringBuilder();
//        for (User s : userService.getAllByReg(reg)) {
//            sb.append(s.toString()).append("\n");
//        }
//        return sb.toString();
//    }
}
