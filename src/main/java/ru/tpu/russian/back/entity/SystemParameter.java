package ru.tpu.russian.back.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "SYSTEM_CONFIG")
@NoArgsConstructor
public class SystemParameter {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "KEY")
    private String key;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "DESCRIPTION")
    private String description;

    public SystemParameter(String name, String key, String value, String description) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.key = key;
        this.value = value;
        this.description = description;
    }
}
