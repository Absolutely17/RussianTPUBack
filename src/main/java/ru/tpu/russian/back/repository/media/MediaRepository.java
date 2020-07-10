package ru.tpu.russian.back.repository.media;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tpu.russian.back.entity.Media;

public interface MediaRepository extends JpaRepository<Media, String> {

    Media getById(String id);
}
