package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.Media;
import ru.tpu.russian.back.repository.media.MediaRepository;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

@Service
@Slf4j
public class MediaService {

    private MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public byte[] getImage(String id) {
        log.info("Get image with ID {}", id);
        Media image = mediaRepository.getById(id);
        log.info("Update last use date image in DB.");
        image.setLastUseDate(new Date());
        mediaRepository.save(image);
        return image.getData();
    }

    // Для тестов

    public byte[] getTestImageInByteArray() throws IOException {
        try {
            File img = new File("C:/test.jpg");
            return Files.readAllBytes(img.toPath());
        } catch (IOException ex) {
            log.error("[TEST] Error in the process of obtaining bytes from the image. Exception - {}", ex.getMessage());
            throw ex;
        }
    }

    public void uploadImage(String nameImg) throws IOException {
        log.info("[TEST] Try to upload to DB image from C:/{}", nameImg);
        File img = new File("C:/" + nameImg);
        Media media = new Media(UUID.randomUUID().toString(), Files.readAllBytes(img.toPath()), new Date(), new Date());
        mediaRepository.save(media);
    }
}
