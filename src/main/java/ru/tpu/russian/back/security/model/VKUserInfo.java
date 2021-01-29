package ru.tpu.russian.back.security.model;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.account.UserSettings;
import lombok.extern.slf4j.Slf4j;
import ru.tpu.russian.back.exception.BusinessException;

import static java.util.Objects.requireNonNull;

@Slf4j
public class VKUserInfo implements OAuthUserInfo {

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
            String email) throws BusinessException {
        try {
            UserActor actor = new UserActor(requireNonNull(userId), token);
            UserSettings user = vk.account().getProfileInfo(actor).execute();
            return new VKUserInfo(requireNonNull(email), user.getFirstName(), user.getLastName());
        } catch (Exception ex) {
            log.error("Problems with access to API VK or incorrect input data.", ex);
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
        return "vk";
    }
}
