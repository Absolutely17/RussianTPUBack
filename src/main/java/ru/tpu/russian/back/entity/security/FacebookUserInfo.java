package ru.tpu.russian.back.entity.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.social.facebook.api.*;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

import javax.security.auth.login.LoginException;

import static ru.tpu.russian.back.entity.security.FacebookUtils.*;

@Slf4j
public class FacebookUserInfo implements OAuthUserInfo {

    private final String provider = "facebook";

    private String email;

    private String firstName;

    private String lastName;

    private FacebookUserInfo(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static FacebookUserInfo create(String token) throws LoginException {
        try {
            Facebook facebook = new FacebookTemplate(token);
            User profile = facebook.fetchObject(LOGGED_USER, User.class, USER_FIELD_EMAIL, USER_FIELD_FIRST_NAME, USER_FIELD_LAST_NAME);
            return new FacebookUserInfo(
                    profile.getEmail(),
                    profile.getFirstName(),
                    profile.getLastName());
        } catch (Exception ex) {
            log.error("Some problems with Facebook API. Exception {}", ex);
            throw new LoginException("Problem with Facebook API or with input data.");
        }
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getProvider() {
        return provider;
    }
}
