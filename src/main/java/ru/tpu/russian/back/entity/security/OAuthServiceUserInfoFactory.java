package ru.tpu.russian.back.entity.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.exception.InternalException;

@Slf4j
public class OAuthServiceUserInfoFactory {

    public static OAuthUserInfo getOAuthUserInfo(
            String provider,
            String token,
            @Nullable Integer userId,
            @Nullable String email) throws InternalException {
        switch (provider) {
            case "google":
                return GoogleOAuthUserInfo.create(token);
            case "facebook":
                return FacebookUserInfo.create(token);
            case "vk":
                return VKUserInfo.create(token, userId, email);
            default:
                log.error("This provider is not supported.");
                throw new InternalException("Exception.login.service.notSupported");
        }
    }
}
