package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.entity.*;

import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/test", produces = APPLICATION_JSON_UTF8_VALUE)
public class TestRest {

	@RequestMapping(method = GET)
	public String getCurrentTime() {
		return "Web-service worked. Current time = " + new Date().toString();
	}
}
