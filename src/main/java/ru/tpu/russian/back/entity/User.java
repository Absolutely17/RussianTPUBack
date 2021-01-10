package ru.tpu.russian.back.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.enums.ProviderType;
import ru.tpu.russian.back.enums.UserGender;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "USER_VIEW")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private String id;

    @Column(name = "PASSWORD", length = 100)
    private String password;

    @Column(name = "FIRST_NAME", length = 50)
    private String firstName;

    @Column(name = "LAST_NAME", length = 50)
    @Nullable
    private String lastName;

    @Column(name = "MIDDLE_NAME", length = 50)
    @Nullable
    private String middleName;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "SEX", length = 20)
    @Nullable
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @Column(name = "LANGUAGE_ID")
    private String language;

    @Column(name = "PHONE_NUMBER", length = 20)
    @Nullable
    private String phoneNumber;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "REFRESH_SALT")
    private String refreshSalt;

    @Column(name = "REGISTRATION_PROVIDER")
    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @Column(name = "CONFIRMED")
    private boolean isConfirm;

    @Nullable
    @Column(name = "GROUP_NAME")
    private String groupName;

    public User(
            String firstName, @Nullable String lastName,
            @Nullable String middleName, @Nullable UserGender gender,
            String language, @Nullable String phoneNumber,
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
