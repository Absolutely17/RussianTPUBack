package ru.tpu.russian.back.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "SYSTEM_CONFIG")
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
}
