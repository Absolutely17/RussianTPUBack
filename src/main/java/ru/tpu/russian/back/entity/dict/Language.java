package ru.tpu.russian.back.entity.dict;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Языки")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Language {

    @Column(name = "ID языка")
    @Id
    private String id;

    @Column(name = "Наименование")
    private String fullName;

    @Column(name = "Сокращенное название языка")
    private String shortName;

    @Column(name = "Пиктограмма языка")
    private String imageId;
}
