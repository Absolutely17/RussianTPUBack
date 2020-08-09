package ru.tpu.russian.back.repository.user;

import ru.tpu.russian.back.entity.User;

import java.util.*;

public interface IUserRepository {

    List<User> getAllByLanguage(Map<String, Object> params);

    List<User> getAllByReg(Map<String, Object> params);

    void saveUser(Map<String, Object> params);

    void editRefreshSalt(String email, String salt);

    Optional<User> getUserByEmail(String email);

    int editRegisteredStatus(String email, boolean status);
}
