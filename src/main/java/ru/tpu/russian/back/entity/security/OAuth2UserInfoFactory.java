package ru.tpu.russian.back.entity.security;

import ru.tpu.russian.back.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) throws OAuth2AuthenticationProcessingException {
        switch (registrationId) {
            case "google":
                return new GoogleOAuth2UserInfo(attributes);
            default:
                throw new OAuth2AuthenticationProcessingException("Sorry. Login with " + registrationId + "isn't supported.");
        }
    }
}
