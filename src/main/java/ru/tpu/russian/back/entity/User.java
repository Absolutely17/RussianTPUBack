package ru.tpu.russian.back.entity;

import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.dto.enums.ProviderType;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

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
    @Nullable
    private String lastName;

    @Column(name = "Отчество")
    @Nullable
    private String middleName;

    @Column(name = "Роль")
    private String role;

    @Column(name = "Пол")
    @Nullable
    private String gender;

    @Column(name = "Язык")
    private String language;

    @Column(name = "Номер телефона")
    @Nullable
    private String phoneNumber;

    @Column(name = "Электронная почта")
    private String email;

    @Column(name = "refresh salt")
    private String refreshSalt;

    @Column(name = "Provider")
    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    public User(
            String firstName, @Nullable String lastName,
            @Nullable String middleName, @Nullable String gender,
            String language, @Nullable String phoneNumber,
            String email, ProviderType provider
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.gender = gender;
        this.language = language;
        this.phoneNumber = phoneNumber;
        this.email = requireNonNull(email, "Email must be filled");
        this.provider = provider;
        role = "ROLE_USER";
    }

    public void setPassword(String encodePassword) {
        password = encodePassword;
    }
}
