package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.repository.media.MediaRepository;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class MediaService {

    private MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public byte[] getImage(String id) {
        return mediaRepository.getById(id).getData();
    }

    public byte[] getImageInBase64(String id) {
        return Base64.getEncoder().encode(mediaRepository.getById(id).getData());
    }

    public byte[] getTestImageInByteArray() throws IOException {
        File img = new File("C:/test.jpg");
        return Files.readAllBytes(img.toPath());
    }
    public byte[] getTestImageInBase64() throws IOException {
        File img = new File("C:/test.jpg");
        return Base64.getEncoder().encode(Files.readAllBytes(img.toPath()));
    }
}
