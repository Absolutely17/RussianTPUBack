package ru.tpu.russian.back.entity.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import javax.security.auth.login.LoginException;

@Slf4j
public class OAuthServiceUserInfoFactory {

    public static OAuthUserInfo getOAuthUserInfo(
            String provider,
            String token,
            @Nullable Integer userId,
            @Nullable String email) throws LoginException {
        switch (provider) {
            case "google":
                return GoogleOAuthUserInfo.create(token);
            case "facebook":
                return FacebookUserInfo.create(token);
            case "vk":
                return VKUserInfo.create(token, userId, email);
            default:
                log.error("This provider is not supported.");
                throw new LoginException("This provider is not supported");
        }
    }
}
