package ru.tpu.russian.back.repository.media;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ru.tpu.russian.back.entity.Media;

import java.util.Date;

public interface MediaRepository extends JpaRepository<Media, String> {

    Media getById(String id);

    @Modifying
    @Query(value = "update Media m set m.lastUseDate = :lastUseDate where m.id = :id")
    void updateLastUseDate(@Param("lastUseDate") Date lastUseDate, @Param("id") String id);
}
