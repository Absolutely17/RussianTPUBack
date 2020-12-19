package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.MediaService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/media")
public class MediaRest {

    private MediaService mediaService;

    public MediaRest(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @RequestMapping(method = GET, path = "/img/{id}")
    public byte[] getImage(
            @PathVariable String id
    ) {
        return mediaService.getImage(id);
    }

    /**
     * Загрузка изображения через админку
     */
    @RequestMapping(
            method = POST, path = "/admin/img/upload",
            consumes = {"multipart/form-data"},
            produces = "text/plain"
    )
    public String uploadImage(@RequestParam("file") MultipartFile file) throws BusinessException {
        return mediaService.uploadImage(file);
    }
}
