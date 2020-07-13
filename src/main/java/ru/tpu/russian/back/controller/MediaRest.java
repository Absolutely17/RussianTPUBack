package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.service.MediaService;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/media", produces = APPLICATION_JSON_UTF8_VALUE)
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
    @ApiOperation(value = "Получение тестового изображения из файловой системы сервера (ДЛЯ ТЕСТОВ)")
    @RequestMapping(method = GET, path = "/test/img/byte")
    public byte[] getTestImageInByteArray() throws IOException {
        return mediaService.getTestImageInByteArray();
    }

    @ApiOperation(value = "Загрузка тестового изображения в БД (ДЛЯ ТЕСТОВ)")
    @RequestMapping(method = GET, path = "/test/uploadImg/{nameImg}")
    public void uploadImage(
            @ApiParam(value = "Название файла с расширением из C:/", required = true)
            @PathVariable String nameImg
    ) throws IOException {
        mediaService.uploadImage(nameImg);
    }
}
