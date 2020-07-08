package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.service.MediaService;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/media", produces = APPLICATION_JSON_UTF8_VALUE)
public class MediaRest {

    private MediaService mediaService;

    public MediaRest(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @RequestMapping(method = GET, path = "/img/{id}")
    public String getImage(
            @PathVariable String id
    ) throws IOException {
        return mediaService.getImage(id);
    }

    @RequestMapping(method = GET, path = "/test/img")
    public byte[] getTestImage() throws IOException {
        return mediaService.getTestImage();
    }

}
