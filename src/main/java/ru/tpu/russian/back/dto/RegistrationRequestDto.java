package ru.tpu.russian.back.dto;

import lombok.*;
import org.springframework.lang.NonNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class RegistrationRequestDto {

    @NonNull
    private String password;

    @NonNull
    private String email;

    @NonNull
    private String firstname;

    private String surname;

    private String patronymic;

    @NonNull
    private boolean sex;

    @NonNull
    private String language;

    private String phoneNumber;

    @Override
    public String toString() {
        return "RegistrationRequestDto{" +
                "email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", sex=" + sex +
                ", language='" + language + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
