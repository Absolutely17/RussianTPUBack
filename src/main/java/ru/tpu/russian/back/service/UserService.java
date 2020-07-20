package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.Jwt.*;
import ru.tpu.russian.back.dto.*;
import ru.tpu.russian.back.dto.enums.ProviderType;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.exception.RegistrationException;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.*;

@Service
@Slf4j
public class UserService {

    private static final Pattern VALID_EMAIL_ADDRESS = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public void register(RegistrationRequestDto registrationRequestDto) throws RegistrationException {
        log.info("Register new user. {}", registrationRequestDto.toString());
        log.info("Check registration parameters on valid.");
        checkRegistrationParams(registrationRequestDto);
        User user = new User(registrationRequestDto);
        log.info("Set role user to ROLE_USER");
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        log.info("Set provider to local");
        user.setProvider(ProviderType.valueOf("local"));
        log.info("Saving new user in DB...");
        register(user);
    }

    private void checkRegistrationParams(RegistrationRequestDto regParams) throws RegistrationException {
        Matcher matcher = VALID_EMAIL_ADDRESS.matcher(regParams.getEmail());
        if (!matcher.find()) {
            throw new RegistrationException("Email address is not in the correct format.");
        }
        Optional<User> userOptional = userRepository.getUserByEmail(regParams.getEmail());
        if (userOptional.isPresent()) {
            throw new RegistrationException("Email address already taken.");
        }
    }

    public void register(User user) {
        log.info("Register new user.");
        Map<String, Object> params = putUserFieldToMap(user);
        userRepository.saveUser(params);
    }

    private Map<String, Object> putUserFieldToMap(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("Password", user.getPassword());
        params.put("Email", user.getEmail());
        params.put("FirstName", user.getFirstName());
        params.put("SecondName", user.getSurname());
        params.put("Sex", user.isSex());
        params.put("Language", user.getLanguage());
        params.put("Role", user.getRole());
        params.put("Patronymic", user.getPatronymic());
        params.put("PhoneNumber", user.getPhoneNumber());
        params.put("Provider", user.getProvider().toString());
        return params;
    }

    public AuthResponseDto login(AuthRequestDto authRequest) {
        log.info("Login in system with email {}", authRequest.getEmail());
        User user = findByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
        if (user != null) {
            String token = jwtProvider.generateToken(user.getEmail());
            if (authRequest.isRememberMe()) {
                log.info("Option rememberMe selected.");
                String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
                return new AuthResponseDto(token, refreshToken, true);
            }
            return new AuthResponseDto(token, true);
        }
        return new AuthResponseDto(false);
    }

    public AuthResponseDto refreshToken(HttpServletRequest servletRequest) {
        log.info("Refresh access token");
        String token = JwtFilter.getTokenFromRequest(servletRequest);
        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            String refreshSaltInToken = jwtProvider.getSaltFromRefreshToken(token);
            String refreshSaltInDB = findByEmail(email).getRefreshSalt();
            if (refreshSaltInDB.equals(refreshSaltInToken)) {
                log.info("Salt matched. Generating new tokens and new salt. Email {}", email);
                return new AuthResponseDto(
                        jwtProvider.generateToken(email),
                        jwtProvider.generateRefreshToken(email),
                        true
                );
            }
        }
        return new AuthResponseDto(false);
    }

    private User findByEmailAndPassword(String email, String password) {
        log.info("Try to find user with email {} in DB", email);
        User user = findByEmail(email);
        if (user != null) {
            log.info("Compare password...");
            if (passwordEncoder.matches(password, user.getPassword())) {
                log.info("The passwords matched. Email {}", email);
                return user;
            } else {
                log.error("Password mismatch");
            }
        }
        return user;
    }

    private User findByEmail(String email) {
        Optional<User> user = userRepository.getUserByEmail(email);
        return user.orElse(null);
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
