package ru.tpu.russian.back.service.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.entity.security.CustomUserDetails;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.persistence.NoResultException;
import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) {
        Optional<User> user = userRepository.getUserByEmail(email);
        if (user.isPresent()) {
            return CustomUserDetails.create(user.get());
        } else {
            log.error("User does not exist.");
            throw new NoResultException("User does not exist");
        }
    }
}
