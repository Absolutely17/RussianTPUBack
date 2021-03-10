package ru.tpu.russian.back.entity.user;

import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.enums.*;

import javax.persistence.*;
import java.util.Date;

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

    @Nullable
    @Column(name = "ACADEMIC_PLAN_URL")
    private String academicPlanUrl;

    @Column(name = "LOAD_DATE")
    private Date loadDate;

    public void setPassword(String encodePassword) {
        password = encodePassword;
    }
}
