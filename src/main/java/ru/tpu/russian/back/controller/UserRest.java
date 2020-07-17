package ru.tpu.russian.back.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.service.UserService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/user")
@Api(tags = {SpringFoxConfig.USER_REST})
public class UserRest {

    private final UserService userService;

    public UserRest(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = GET, path = "/language")
    public String getUsersByLanguage(@RequestParam("language") String language) {
        StringBuilder sb = new StringBuilder();
        for (User s : userService.getAllByLanguage(language)) {
            sb.append(s.toString()).append("\n");
        }
        return sb.toString();
    }

    @RequestMapping(method = GET, path = "/reg")
    public String getUsersByReg(@RequestParam("reg") boolean reg) {
        StringBuilder sb = new StringBuilder();
        for (User s : userService.getAllByReg(reg)) {
            sb.append(s.toString()).append("\n");
        }
        return sb.toString();
    }
}
