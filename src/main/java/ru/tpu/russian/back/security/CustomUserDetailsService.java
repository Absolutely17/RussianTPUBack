package ru.tpu.russian.back.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.repository.user.UserRepository;
import ru.tpu.russian.back.security.model.CustomUserDetails;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Nullable
    public CustomUserDetails loadUserByUsername(String email) {
        Optional<CustomUserDetails> user = userRepository.getUserDetailByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            log.error("User {} does not exist in DB.", email);
            return null;
        }
    }
}
