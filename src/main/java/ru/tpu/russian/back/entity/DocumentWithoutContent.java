package ru.tpu.russian.back.entity;

import javax.persistence.*;

@Entity
@Table(name = "Документы")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "GetDocumentsWithoutContent",
                procedureName = "GetDocumentsWithoutContent",
                resultClasses = {DocumentWithoutContent.class},
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class)
                })
})
public class DocumentWithoutContent extends Document {

}
