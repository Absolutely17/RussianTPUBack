package ru.tpu.russian.back.entity;

import javax.persistence.*;

@Entity
@Table(name = "UTIL")
public class UtilParameter {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "NAME")
    private String name;
}
