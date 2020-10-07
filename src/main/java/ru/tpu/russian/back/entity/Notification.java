package ru.tpu.russian.back.entity;

import lombok.*;

import javax.persistence.*;

@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "AddGroupNotification",
                procedureName = "AddGroupNotification",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Language", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Email", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Status", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Title", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Message", type = String.class)
                }),
        @NamedStoredProcedureQuery(
                name = "AddUsersNotification",
                procedureName = "AddUsersNotification",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Users", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Email", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Status", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Title", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Message", type = String.class)
                })
})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Notification {

    @Id
    private String idMessage;

    private String title;

    private String message;

    private String emailAdmin;

    private String status;

    private String users;
}
