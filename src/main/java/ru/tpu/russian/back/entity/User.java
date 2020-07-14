package ru.tpu.russian.back.entity;

import lombok.*;

import javax.persistence.*;

@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "GetRegistered",
                procedureName = "GetRegistered",
                resultClasses = {User.class},
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Reg", type = boolean.class)}
        ),
        @NamedStoredProcedureQuery(
                name = "GetUserByLanguage",
                procedureName = "GetUserByLanguage",
                resultClasses = {User.class},
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Language", type = String.class)
                })
})

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "Имя")
    private String surname;

    @Column(name = "Фамилия")
    private String firstName;

    @Column(name = "Отчество")
    private String patronymic;

    @Column(name = "Роль")
    private String role;

    @Column(name = "Пол")
    private boolean sex;

    @Column(name = "Регистрация")
    private boolean registration;

    @Column(name = "Язык")
    private String language;

    @Column(name = "email")
    private String email;

    @Column(name = "Номер телефона")
    private String phoneNumber;
}
