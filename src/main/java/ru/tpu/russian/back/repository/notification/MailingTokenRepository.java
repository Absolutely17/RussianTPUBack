package ru.tpu.russian.back.repository.notification;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.notification.MailingToken;

import java.util.Optional;

@Repository
public interface MailingTokenRepository extends JpaRepository<MailingToken, String> {

    Optional<MailingToken> getByUserIdAndActive(String id, boolean active);

    @Query("select active from MailingToken where userId = :userId")
    Boolean isActiveByUserId(@Param("userId") String userid);
}
