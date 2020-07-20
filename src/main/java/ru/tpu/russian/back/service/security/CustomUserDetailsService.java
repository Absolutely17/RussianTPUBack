package ru.tpu.russian.back.service.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.entity.security.CustomUserDetails;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.persistence.NoResultException;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) {
        Optional<User> user = userRepository.getUserByEmail(email);
        return CustomUserDetails.create(user.orElseThrow(() -> new NoResultException("User does not exist")));
    }
}
