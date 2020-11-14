package ru.tpu.russian.back.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Notification {

    @Id
    private String idMessage;

    private String title;

    private String message;

    private String emailAdmin;

    private String status;

    private String users;
}
