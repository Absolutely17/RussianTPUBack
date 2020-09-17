package ru.tpu.russian.back.entity;

import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.enums.*;

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
                name = "EditRegisteredStatus",
                procedureName = "EditRegisteredStatus",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "status", type = boolean.class)
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
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "Provider", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "verified", type = boolean.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "groupName", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "EditUser",
                procedureName = "EditUser",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "psw", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "firstName", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "lang", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "secondName", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "patronymic", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "sex", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "phoneNum", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "groupName", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "EditPasswordUser",
                procedureName = "EditPasswordUser",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "token", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "newPassword", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "AddResetPasswordRequest",
                procedureName = "AddResetPasswordRequest",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "token", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "GetUserGroupID",
                procedureName = "GetUserGroupID",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "internalGroupID", type = String.class)
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

    @Column(name = "Пароль", length = 100)
    private String password;

    @Column(name = "Имя", length = 50)
    private String firstName;

    @Column(name = "Фамилия", length = 50)
    @Nullable
    private String lastName;

    @Column(name = "Отчество", length = 50)
    @Nullable
    private String middleName;

    @Column(name = "Роль")
    private String role;

    @Column(name = "Пол", length = 20)
    @Nullable
    private String gender;

    @Column(name = "Язык", length = 20)
    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(name = "Номер телефона", length = 20)
    @Nullable
    private String phoneNumber;

    @Column(name = "Электронная почта", length = 100)
    private String email;

    @Column(name = "refresh salt")
    private String refreshSalt;

    @Column(name = "Provider")
    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @Column(name = "Подтвержден")
    private boolean isConfirm;

    @Column(name = "Номер группы")
    @Nullable
    private String groupName;

    public User(
            String firstName, @Nullable String lastName,
            @Nullable String middleName, @Nullable String gender,
            Language language, @Nullable String phoneNumber,
            String email, ProviderType provider, @Nullable String groupName
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.gender = gender;
        this.language = language;
        this.phoneNumber = phoneNumber;
        this.email = requireNonNull(email, "Email must be filled");
        this.provider = provider;
        this.groupName = groupName;
        role = "ROLE_USER";
    }

    public void setPassword(String encodePassword) {
        password = encodePassword;
    }

    public void setConfirm(boolean isConfirm) {
        this.isConfirm = isConfirm;
    }
}
