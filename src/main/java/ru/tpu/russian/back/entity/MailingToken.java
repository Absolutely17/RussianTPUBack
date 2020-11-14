package ru.tpu.russian.back.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USER_NOTIFICATION_TOKEN")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MailingToken {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "TOKEN")
    private String fcmToken;

    @Column(name = "ACTIVE")
    private boolean isActive;

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
