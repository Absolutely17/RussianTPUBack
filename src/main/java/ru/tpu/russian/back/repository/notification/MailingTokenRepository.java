package ru.tpu.russian.back.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tpu.russian.back.entity.MailingToken;

public interface MailingTokenRepository extends JpaRepository<MailingToken, String> {

    MailingToken getByUserId(String id);
}
