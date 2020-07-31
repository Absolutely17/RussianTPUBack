package ru.tpu.russian.back.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Медиа")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @Id
    @Column(name = "Id медиа")
    private String id;

    @Column(name = "Данные")
    private byte[] data;

    @Column(name = "Время создания")
    private Date createDate;

    @Column(name = "Дата последнего обращения")
    private Date lastUseDate;

    public void setLastUseDate(Date newDate) {
        lastUseDate = newDate;
    }
}
