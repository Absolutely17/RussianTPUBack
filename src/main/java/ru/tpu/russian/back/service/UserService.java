package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.interceptor.SimpleKey;
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

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static ru.tpu.russian.back.enums.ProviderType.valueOf;
import static ru.tpu.russian.back.service.MailService.TypeMessages.CONFIRMATION_MESSAGE;
import static ru.tpu.russian.back.service.MailService.TypeMessages.RESET_PASSWORD_MESSAGE;

@Service
@Slf4j
public class UserService {

    private static final int HTTP_STATUS_REG_NEED_FILL = 210;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final MailService mailService;

    private final CacheManager cacheManager;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            MailService mailService,
            CacheManager cacheManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.mailService = mailService;
        this.cacheManager = cacheManager;
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
            mailService.sendMessage(
                    CONFIRMATION_MESSAGE,
                    registrationRequestDto.getEmail(),
                    registrationRequestDto.getLanguage()
            );
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
                request.getProvider(),
                request.getGroupName()
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
        params.put("groupName", user.getGroupName());
        return params;
    }

    public AuthResponseDto login(AuthRequestDto authRequest) throws BusinessException {
        log.info("Login in system with email {}", authRequest.getEmail());
        User user = findByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
        String token = jwtProvider.generateAccessToken(user.getEmail());
        if (authRequest.isRememberMe()) {
            log.debug("Option rememberMe selected.");
            String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
            return new AuthResponseDto(token, refreshToken, new UserResponseDto(user));
        }
        return new AuthResponseDto(token, new UserResponseDto(user));
    }

    public User findByEmailAndPassword(String email, String password) throws BusinessException {
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
            if (!user.getProvider().equals(valueOf(authRequest.getProvider()))) {
                log.warn("It looks like you are trying to authenticate with the wrong service." +
                        " You are trying to login with {}, but you need to login with {}", authRequest.getProvider(), user.getProvider());
                if (ProviderType.local.equals(user.getProvider())) {
                    throw new BusinessException("Exception.login.conflict.providerAndLocal", authRequest.getProvider());
                } else {
                    throw new BusinessException("Exception.login.service.wrongService",
                            authRequest.getProvider(), user.getProvider());
                }
            }
            String token = jwtProvider.generateAccessToken(user.getEmail());
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

    @CacheEvict(value = "menu_items", key = "#requestDto.language + #requestDto.email")
    public void editUser(BaseUserRequestDto requestDto) throws BusinessException {
        log.info("Edit user {}, new data {}.", requestDto.getEmail(), requestDto.toString());
        User userToEdit = findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());
        Map<String, Object> paramsToProcedure = putEditedUserFieldToMap(requestDto);
        Cache cache = cacheManager.getCache("menu_items");
        if (cache != null) {
            cache.evict(new SimpleKey(userToEdit.getLanguage().toString(), userToEdit.getEmail()));
        }
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
        paramsToProcedure.put("groupName", requestDto.getGroupName());
        return paramsToProcedure;
    }

    public UserProfileResponse getUserProfile(String email) throws BusinessException {
        log.info("Get user profile {}", email);
        return new UserProfileResponse(userRepository.getUserByEmail(email).orElseThrow(
                () -> new BusinessException("Exception.login.user.notFound", email)));
    }

    /**
     * Данный рест отправляет письмо на почту пользователя с ссылкой на сброс пароля
     *
     * @param email
     */
    public void resetPasswordRequest(String email) throws BusinessException {
        log.info("Send request to reset password {}", email);
        User user = findByEmail(email);
        if (user == null) {
            log.error("User is not found.");
            throw new BusinessException("Exception.login.user.notFound", email);
        } else {
            try {
                mailService.sendMessage(RESET_PASSWORD_MESSAGE, email, user.getLanguage());
            } catch (Exception ex) {
                log.error("Error in sending reset password mail.", ex);
                throw new BusinessException("Exception.mail.send");
            }
        }
    }

    /**
     * Данный рест взаимодействует с админкой. Выполняет непосредственную смену пароля.
     *
     * @param resetDto новые данные
     */
    public void resetPassword(ResetPasswordDto resetDto) throws BusinessException {
        log.info("Starting to reset password. Token {}", resetDto.getToken());
        String token = resetDto.getToken();
        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            log.info("Trying to reset and edit password on {}", email);
            int success = userRepository.resetAndEditPassword(
                    email,
                    passwordEncoder.encode(resetDto.getPassword()),
                    sha1Hex(token)
            );
            log.info("Reset and editing password {}", success > 0 ? "success" : "failed");
        } else {
            throw new BusinessException("Exception.resetPassword.tokenExpired");
        }
    }
}
