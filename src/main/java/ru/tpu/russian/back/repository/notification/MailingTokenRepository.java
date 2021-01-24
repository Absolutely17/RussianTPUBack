package ru.tpu.russian.back.repository.notification;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.notification.MailingToken;

@Repository
public interface MailingTokenRepository extends JpaRepository<MailingToken, String> {

    MailingToken getByUserIdAndActive(String id, boolean active);

    @Query("select isActive from MailingToken where userId = :userId")
    Boolean isActiveByUserId(@Param("userId") String userid);
}
