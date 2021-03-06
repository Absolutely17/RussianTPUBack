package ru.tpu.russian.back.entity.document;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DOCUMENT")
public class DocumentWithContent extends DocumentSuperClass {

    @Column(name = "CONTENT")
    private byte[] content;
}
