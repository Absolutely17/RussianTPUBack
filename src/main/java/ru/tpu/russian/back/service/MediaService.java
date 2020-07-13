package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.Media;
import ru.tpu.russian.back.repository.media.MediaRepository;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

@Service
public class MediaService {

    private MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public byte[] getImage(String id) {
        return mediaRepository.getById(id).getData();
    }

    // Для тестов

    public byte[] getTestImageInByteArray() throws IOException {
        File img = new File("C:/test.jpg");
        return Files.readAllBytes(img.toPath());
    }

    public void uploadImage(String nameImg) throws IOException {
        File img = new File("C:/" + nameImg);
        Media media = new Media(UUID.randomUUID().toString(), Files.readAllBytes(img.toPath()), new Date());
        mediaRepository.save(media);
    }
}
