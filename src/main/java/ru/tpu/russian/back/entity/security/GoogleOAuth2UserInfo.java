package ru.tpu.russian.back.entity.security;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getFirstName() {
        return (String) attributes.get("given_name");
    }

    @Override
    public String getSurname() {
        if (attributes.containsKey("family_name")) {
            return (String) attributes.get("family_name");
        } else {
            return null;
        }
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
