package ru.tpu.russian.back.entity.notification;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "USER_NOTIFICATION_TOKEN")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MailingToken {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "TOKEN")
    private String fcmToken;

    @Column(name = "ACTIVE")
    private boolean active;

    public MailingToken(String userId, String fcmToken, boolean active) {
        id = UUID.randomUUID().toString();
        this.userId = userId;
        this.fcmToken = fcmToken;
        this.active = active;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }
}
