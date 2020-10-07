package ru.tpu.russian.back.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Токены рассылки")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MailingToken {

    @Id
    @Column(name = "ID пользователя")
    private String userId;

    @Column(name = "Токен")
    private String fcmToken;

    @Column(name = "Активен")
    private boolean isActive;

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
