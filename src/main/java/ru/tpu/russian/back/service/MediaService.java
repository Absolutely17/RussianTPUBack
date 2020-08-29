package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.entity.Media;
import ru.tpu.russian.back.repository.media.MediaRepository;

import java.util.Date;

@Service
@Slf4j
public class MediaService {

    private MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Transactional
    public byte[] getImage(String id) {
        Media image = mediaRepository.getById(id);
        if (image == null) {
            return null;
        } else {
            Date currentTime = new Date();
            image.setLastUseDate(currentTime);
            mediaRepository.save(image);
            return image.getData();
        }
    }
}
