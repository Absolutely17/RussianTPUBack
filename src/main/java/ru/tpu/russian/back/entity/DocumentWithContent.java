package ru.tpu.russian.back.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "Документы")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "GetDocumentsWithContent",
                procedureName = "GetDocumentsWithContent",
                resultClasses = {DocumentWithContent.class},
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "documentId", type = String.class)
                })
})
public class DocumentWithContent extends Document {

    @Column(name = "Содержимое документа")
    private byte[] content;
}
