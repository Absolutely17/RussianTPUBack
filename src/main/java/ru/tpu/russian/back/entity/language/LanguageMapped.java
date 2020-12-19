package ru.tpu.russian.back.entity.language;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class LanguageMapped {

    @Column(name = "ID")
    @Id
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SHORT_NAME")
    private String shortName;

}
