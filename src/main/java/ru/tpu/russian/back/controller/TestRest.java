package ru.tpu.russian.back.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;

import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/test")
@Api(tags = {SpringFoxConfig.TEST_REST})
public class TestRest {

    @RequestMapping(method = GET)
    public String getCurrentTime() {
        return "Web-service worked. Current time = " + new Date().toString();
    }

    @RequestMapping(method = GET, path = "/permissions")
    public String testPermissions() {
        return "You have permissions.";
    }
}
