package ru.tpu.russian.back.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Медиа")
public class Media {

    @Id
    @Column(name = "Id медиа")
    private String id;

    @Column(name = "Данные")
    private byte[] data;

    @Column(name = "Время создания")
    private Date createDate;

    public Media() {
    }

    public Media(String id, byte[] data, Date createDate) {
        this.id = id;
        this.data = data;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public Date getCreateDate() {
        return createDate;
    }
}
