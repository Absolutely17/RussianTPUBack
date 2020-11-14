package ru.tpu.russian.back.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MEDIA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "CONTENT")
    private byte[] data;

    @Column(name = "LOAD_DATE")
    private Date createDate;

    @Column(name = "LAST_USE_DATE")
    private Date lastUseDate;

    public void setLastUseDate(Date newDate) {
        lastUseDate = newDate;
    }
}
