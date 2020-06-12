package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.repository.user.UserRepository;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllByLanguage(String language) {
        Map<String, Object> params = new HashMap<>();
        params.put("Language", language);
        return userRepository.getAllByLanguage(params);
    }

    public List<User> getAllByReg(boolean reg) {
        Map<String, Object> params = new HashMap<>();
        params.put("Reg", reg);
        return userRepository.getAllByReg(params);
    }
}
