package ru.tpu.russian.back.entity.security;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.account.UserSettings;
import lombok.extern.slf4j.Slf4j;

import javax.security.auth.login.LoginException;

@Slf4j
public class VKUserInfo implements OAuthUserInfo {

    private final String provider = "vk";

    private static final VkApiClient vk = new VkApiClient(new HttpTransportClient());

    private String email;

    private String firstName;

    private String lastName;

    private VKUserInfo(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static VKUserInfo create(
            String token,
            Integer userId,
            String email) throws LoginException {
        if (userId == null || email == null) {
            log.error("UserID and Email must not be empty");
            throw new LoginException("UserID and Email must not be empty through VK auth");
        }
        try {
            UserActor actor = new UserActor(userId, token);
            UserSettings user = vk.account().getProfileInfo(actor).execute();
            return new VKUserInfo(email, user.getFirstName(), user.getLastName());
        } catch (Exception ex) {
            log.error("Problems with access to API VK or incorrect input data. Exception {}", ex);
            throw new LoginException("Problems with access to API VK or incorrect input data.");
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
