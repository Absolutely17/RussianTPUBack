package ru.tpu.russian.back.repository.user;

import ru.tpu.russian.back.entity.User;

import java.util.*;

public interface IUserRepository {

    void saveUser(Map<String, Object> params);

    void editRefreshSalt(String email, String salt);

    Optional<User> getUserByEmail(String email);

    int editRegisteredStatus(String email, boolean status);

    void editUser(Map<String, Object> params);

    int resetAndEditPassword(String email, String newPassword, String token);

    void addResetToken(String email, String token);

    Optional<String> getGroupId(String email);
}
