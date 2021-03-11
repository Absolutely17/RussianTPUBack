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

    @Column(name = "DISABLED")
    private boolean disabled;

    @Column(name = "TYPE")
    private String type;

    public SystemParameter(String name, String key, String value, String description, boolean disabled, String type) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.key = key;
        this.value = value;
        this.description = description;
        this.disabled = disabled;
        this.type = type;
    }
}
