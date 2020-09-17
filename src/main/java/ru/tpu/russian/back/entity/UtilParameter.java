package ru.tpu.russian.back.entity;

import javax.persistence.*;

@Entity
@NamedStoredProcedureQuery(
        name = "GetUtilParameter",
        procedureName = "GetUtilParameter",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "key", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "parameter", type = String.class)
        })
public class UtilParameter {

    @Id
    private String id;
}
