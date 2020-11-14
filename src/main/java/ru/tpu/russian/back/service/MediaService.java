package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.tpu.russian.back.entity.Media;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.repository.media.MediaRepository;

import java.io.IOException;
import java.util.Date;

@Service
@Slf4j
public class MediaService {

    private MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Transactional
    @Cacheable(value = "image")
    public byte[] getImage(String id) {
        Media image = mediaRepository.getById(id);
        if (image == null) {
            return null;
        } else {
            Date currentTime = new Date();
            mediaRepository.updateLastUseDate(currentTime, id);
            return image.getData();
        }
    }

    public String uploadImage(MultipartFile file) throws BusinessException {
        try {
            return mediaRepository.upload(file.getBytes());
        } catch (IOException ex) {
            throw new BusinessException("Problem with upload image.");
        }
    }
}
