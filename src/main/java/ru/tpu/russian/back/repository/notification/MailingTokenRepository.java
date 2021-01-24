package ru.tpu.russian.back.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.notification.MailingToken;

import java.util.Optional;

@Repository
public interface MailingTokenRepository extends JpaRepository<MailingToken, String> {

    Optional<MailingToken> getByUserIdAndActive(String id, boolean active);

    int countByIdAndActive(String userid, boolean active);
}
