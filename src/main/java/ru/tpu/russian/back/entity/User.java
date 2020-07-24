package ru.tpu.russian.back.entity;

import lombok.*;
import ru.tpu.russian.back.dto.*;
import ru.tpu.russian.back.dto.enums.ProviderType;

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
                }),
        @NamedStoredProcedureQuery(
                name = "EditUserRefreshSalt",
                procedureName = "EditUserRefreshSalt",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Salt", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "GetUserByEmail",
                procedureName = "GetUserByEmail",
                resultClasses = {User.class},
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "AddUser",
                procedureName = "AddUser",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Password", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Email", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "FirstName", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "SecondName", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Sex", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Language", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Role", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Patronymic", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "PhoneNumber", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Provider", type = String.class)
                }
        )
})

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "UserInfo")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id Пользователя")
    private String id;

    @Column(name = "Пароль")
    private String password;

    @Column(name = "Имя")
    private String firstName;

    @Column(name = "Фамилия")
    private String lastName;

    @Column(name = "Отчество")
    private String patronymic;

    @Column(name = "Роль")
    private String role;

    @Column(name = "Пол")
    private String gender;

    @Column(name = "Язык")
    private String language;

    @Column(name = "Номер телефона")
    private String phoneNumber;

    @Column(name = "Электронная почта")
    private String email;

    @Column(name = "refresh salt")
    private String refreshSalt;

    @Column(name = "Provider")
    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    public User(RegistrationRequestDto registrationRequest) {
        lastName = registrationRequest.getLastName();
        firstName = registrationRequest.getFirstName();
        patronymic = registrationRequest.getPatronymic();
        gender = registrationRequest.getGender();
        language = registrationRequest.getLanguage();
        phoneNumber = registrationRequest.getPhoneNumber();
        email = registrationRequest.getEmail();
    }

    public User(RegistrationRequestServiceDto request) {
        email = request.getEmail();
        firstName = request.getFirstName();
        lastName = request.getLastName();
        patronymic = request.getPatronymic();
        gender = request.getGender();
        language = request.getLanguage();
        phoneNumber = request.getPhoneNumber();
        provider = request.getProvider();
    }
}
