package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.service.MediaService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/media")
@Api(tags = {SpringFoxConfig.MEDIA_REST})
public class MediaRest {

    private MediaService mediaService;

    public MediaRest(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @ApiOperation(value = "Получене изображения из БД")
    @RequestMapping(method = GET, path = "/img/{id}")
    public byte[] getImage(
            @ApiParam(value = "ID изображения", required = true)
            @PathVariable String id
    ) {
        return mediaService.getImage(id);
    }
}
