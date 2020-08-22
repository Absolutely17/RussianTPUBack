package ru.tpu.russian.back.entity.security;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import ru.tpu.russian.back.exception.BusinessException;

import java.util.Collections;

@Slf4j
public class GoogleOAuthUserInfo implements OAuthUserInfo {

    private String email;

    private String firstName;

    private String lastName;

    private static final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
            new ApacheHttpTransport(),
            new JacksonFactory())
            .setAudience(Collections.singletonList("827029366413-ckl58bbhvkl57qq2f4vnejpoijig0r2s.apps.googleusercontent.com"))
            .build();

    private GoogleOAuthUserInfo(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static GoogleOAuthUserInfo create(String token) throws BusinessException {
        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                Payload payload = idToken.getPayload();
                return new GoogleOAuthUserInfo(
                        payload.getEmail(),
                        (String) payload.get("given_name"),
                        (String) payload.get("family_name"));
            } else {
                log.error("Token invalid.");
                throw new BusinessException("Exception.login.service.invalidToken");
            }
        } catch (Exception ex) {
            log.error("Problem with verifying token", ex);
            throw new BusinessException("Exception.login.service.internalProblem");
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
        return "google";
    }
}
