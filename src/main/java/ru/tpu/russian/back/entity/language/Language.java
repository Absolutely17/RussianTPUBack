package ru.tpu.russian.back.entity.language;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "LANGUAGE")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Language extends LanguageMapped {

    @Column(name = "ICON_ID")
    private String imageId;
}
