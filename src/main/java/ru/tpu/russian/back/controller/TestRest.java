package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/test")
public class TestRest {

    @RequestMapping(method = GET)
    public String getCurrentTime() {
        return "Web-service worked. Current time = " + new Date().toString();
    }
}
