package ru.tpu.russian.back.repository.notification;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.MailingToken;

@Repository
public interface MailingTokenRepository extends JpaRepository<MailingToken, String> {

    MailingToken getByUserId(String id);

    @Query("select isActive from MailingToken where userId = :userId")
    boolean isActiveByUserId(@Param("userId") String userid);
}
