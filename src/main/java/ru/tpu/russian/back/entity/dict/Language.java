package ru.tpu.russian.back.entity.dict;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "LANGUAGE")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Language {

    @Column(name = "ID")
    @Id
    private String id;

    @Column(name = "NAME")
    private String fullName;

    @Column(name = "SHORT_NAME")
    private String shortName;

    @Column(name = "ICON_ID")
    private String imageId;
}
