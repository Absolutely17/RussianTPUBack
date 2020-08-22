package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.request.*;
import ru.tpu.russian.back.dto.response.*;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.entity.security.*;
import ru.tpu.russian.back.enums.ProviderType;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.repository.user.UserRepository;

import java.util.*;

@Service
@Slf4j
public class UserService {

    private static final int HTTP_STATUS_REG_NEED_FILL = 210;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final MailService mailService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            MailService mailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.mailService = mailService;
    }

    public void register(BaseUserRequestDto registrationRequestDto) throws BusinessException {
        log.info("Register new user. {}", registrationRequestDto.toString());
        log.debug("Check registration parameters on valid.");
        checkEmailOnExist(registrationRequestDto.getEmail());
        User user = convertRegRequestToUser(registrationRequestDto);
        user.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        log.debug("Saving new user in DB.");
        register(user);
        try {
            mailService.sendMessage(registrationRequestDto.getEmail(), registrationRequestDto.getFirstName());
        } catch (Exception ex) {
            log.warn("Register success. But some problem with sending confirm email.", ex);
        }
    }

    private void checkEmailOnExist(String email) throws BusinessException {
        log.debug("Check email address for availability in the database.");
        Optional<User> userOptional = userRepository.getUserByEmail(email);
        if (userOptional.isPresent()) {
            log.warn("This email address already taken.");
            throw new BusinessException("Exception.registration.email.exist", email);
        }
    }

    private static User convertRegRequestToUser(BaseUserRequestDto request) {
        return new User(
                request.getFirstName(),
                request.getLastName(),
                request.getMiddleName(),
                request.getGender(),
                request.getLanguage(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getProvider()
        );
    }

    private void register(User user) {
        log.info("Register new user.");
        Map<String, Object> params = putUserFieldToRegMap(user);
        userRepository.saveUser(params);
    }

    private static Map<String, Object> putUserFieldToRegMap(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("Password", user.getPassword());
        params.put("Email", user.getEmail());
        params.put("FirstName", user.getFirstName());
        params.put("SecondName", user.getLastName());
        params.put("Sex", user.getGender());
        params.put("Language", user.getLanguage().toString());
        params.put("Role", user.getRole());
        params.put("Patronymic", user.getMiddleName());
        params.put("PhoneNumber", user.getPhoneNumber());
        params.put("Provider", user.getProvider().toString());
        params.put("verified", user.isConfirm());
        return params;
    }

    public AuthResponseDto login(AuthRequestDto authRequest) throws BusinessException {
        log.info("Login in system with email {}", authRequest.getEmail());
        User user = findByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
        String token = jwtProvider.generateToken(user.getEmail());
        if (authRequest.isRememberMe()) {
            log.debug("Option rememberMe selected.");
            String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
            return new AuthResponseDto(token, refreshToken, new UserResponseDto(user));
        }
        return new AuthResponseDto(token, new UserResponseDto(user));
    }

    private User findByEmailAndPassword(String email, String password) throws BusinessException {
        log.debug("Try to find user with email {} in DB", email);
        User user = findByEmail(email);
        if (user != null) {
            log.debug("User founded. Compare password.");
            if (passwordEncoder.matches(password, user.getPassword())) {
                log.info("The passwords matched. Email {}", email);
                return user;
            } else {
                log.warn("Password mismatch");
                throw new BusinessException("Exception.login.password.mismatch");
            }
        } else {
            log.error("User is not found.");
            throw new BusinessException("Exception.login.user.notFound", email);
        }
    }

    private User findByEmail(String email) {
        Optional<User> user = userRepository.getUserByEmail(email);
        return user.orElse(null);
    }

    public ResponseEntity<?> loginWithService(AuthRequestWithServiceDto authRequest) throws BusinessException {
        log.info("Login in system with service {}", authRequest.getProvider());
        OAuthUserInfo userInfo = OAuthServiceUserInfoFactory.getOAuthUserInfo(
                authRequest.getProvider(),
                authRequest.getToken(),
                authRequest.getUserId(),
                authRequest.getEmail()
        );
        User user = findByEmail(userInfo.getEmail());
        if (user != null) {
            if (!user.getProvider().equals(ProviderType.valueOf(authRequest.getProvider()))) {
                throw new BusinessException("Exception.login.service.wrongService",
                        authRequest.getProvider(), user.getProvider());
            }
            String token = jwtProvider.generateToken(user.getEmail());
            String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
            AuthResponseDto response = new AuthResponseDto(token, refreshToken, new UserResponseDto(user));
            return new ResponseEntity<>(
                    response, HttpStatus.OK
            );
        } else {
            log.info("This user does not exist in the database. Need register.");
            return ResponseEntity.status(HTTP_STATUS_REG_NEED_FILL)
                    .headers(new HttpHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userInfo);
            }
    }

    public void registerWithService(BaseUserRequestDto registrationRequest) throws BusinessException {
        log.info("Register new user through service. User {}", registrationRequest.toString());
        log.debug("Convert to entity User.");
        checkEmailOnExist(registrationRequest.getEmail());
        User user = convertRegRequestToUser(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setConfirm(true);
        log.debug("Saving new user in DB.");
        register(user);
    }

    public void editUser(BaseUserRequestDto requestDto) throws BusinessException {
        User userToEdit = findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());
        Map<String, Object> paramsToProcedure = putEditedUserFieldToMap(requestDto);
        userRepository.editUser(paramsToProcedure);
    }

    private Map<String, Object> putEditedUserFieldToMap(BaseUserRequestDto requestDto) {
        Map<String, Object> paramsToProcedure = new HashMap<>();
        paramsToProcedure.put("email", requestDto.getEmail());
        paramsToProcedure.put("psw", requestDto.getNewPassword() != null ?
                passwordEncoder.encode(requestDto.getNewPassword()) : null);
        paramsToProcedure.put("firstName", requestDto.getFirstName());
        paramsToProcedure.put("lang", requestDto.getLanguage().toString());
        paramsToProcedure.put("secondName", requestDto.getLastName());
        paramsToProcedure.put("patronymic", requestDto.getMiddleName());
        paramsToProcedure.put("sex", requestDto.getGender());
        paramsToProcedure.put("phoneNum", requestDto.getPhoneNumber());
        return paramsToProcedure;
    }

    public UserResponseDto getUserProfile(String email) throws BusinessException {
        return new UserResponseDto(userRepository.getUserByEmail(email).orElseThrow(
                () -> new BusinessException("Exception.login.user.notFound", email)));
    }
}
