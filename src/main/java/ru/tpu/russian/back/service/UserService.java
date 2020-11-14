package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.SimpleNameObj;
import ru.tpu.russian.back.dto.auth.*;
import ru.tpu.russian.back.dto.mapper.UserMapper;
import ru.tpu.russian.back.dto.notification.NotificationTokenRequest;
import ru.tpu.russian.back.dto.user.*;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.entity.notification.MailingToken;
import ru.tpu.russian.back.entity.security.*;
import ru.tpu.russian.back.enums.ProviderType;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.repository.dicts.IDictRepository;
import ru.tpu.russian.back.repository.notification.MailingTokenRepository;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static ru.tpu.russian.back.enums.ProviderType.valueOf;
import static ru.tpu.russian.back.service.MailService.TypeMessages.CONFIRMATION_MESSAGE;
import static ru.tpu.russian.back.service.MailService.TypeMessages.RESET_PASSWORD_MESSAGE;

@Service
@Slf4j
public class UserService {

    private static final int HTTP_STATUS_REG_NEED_FILL = 210;

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final MailService mailService;

    private final CacheManager cacheManager;

    private final MailingTokenRepository mailingTokenRepository;

    private final UserMapper userMapper;

    private final IDictRepository dictRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            MailService mailService,
            CacheManager cacheManager,
            MailingTokenRepository mailingTokenRepository,
            IDictRepository dictRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.mailService = mailService;
        this.cacheManager = cacheManager;
        this.mailingTokenRepository = mailingTokenRepository;
        this.userMapper = userMapper;
        this.dictRepository = dictRepository;
    }

    public void register(BaseUserRequest registrationRequestDto) throws BusinessException {
        log.info("Register new user. {}", registrationRequestDto.toString());
        log.debug("Check registration parameters on valid.");
        checkEmailOnExist(registrationRequestDto.getEmail());
        User user = userMapper.convertToUserFromRegistrationRequest(registrationRequestDto);
        user.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        log.debug("Saving new user in DB.");
        userRepository.saveUser(user);
        try {
            mailService.sendMessage(
                    CONFIRMATION_MESSAGE,
                    registrationRequestDto.getEmail(),
                    registrationRequestDto.getLanguageId()
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

    public AuthResponse login(AuthRequest authRequest) throws BusinessException {
        log.info("Login in system with email {}", authRequest.getEmail());
        User user = findByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
        String token = jwtProvider.generateAccessToken(user.getEmail());
        if (authRequest.isRememberMe()) {
            log.debug("Option rememberMe selected.");
            String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
            return new AuthResponse(token, refreshToken, userMapper.convertToResponse(user));
        }
        return new AuthResponse(token, userMapper.convertToResponse(user));
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

    public ResponseEntity<?> loginWithService(AuthWithServiceRequest authRequest) throws BusinessException {
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
                log.warn(
                        "It looks like you are trying to authenticate with the wrong service." +
                                " You are trying to login with {}, but you need to login with {}",
                        authRequest.getProvider(),
                        user.getProvider()
                );
                if (ProviderType.local.equals(user.getProvider())) {
                    throw new BusinessException("Exception.login.conflict.providerAndLocal", authRequest.getProvider());
                } else {
                    throw new BusinessException("Exception.login.service.wrongService",
                            authRequest.getProvider(), user.getProvider());
                }
            }
            String token = jwtProvider.generateAccessToken(user.getEmail());
            String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
            AuthResponse response = new AuthResponse(token, refreshToken, userMapper.convertToResponse(user));
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

    public void registerWithService(BaseUserRequest registrationRequest) throws BusinessException {
        log.info("Register new user through service. User {}", registrationRequest.toString());
        log.debug("Convert to entity User.");
        checkEmailOnExist(registrationRequest.getEmail());
        User user = userMapper.convertToUserFromRegistrationRequest(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setConfirm(true);
        log.debug("Saving new user in DB.");
        userRepository.saveUser(user);
    }

    public void editUser(BaseUserRequest requestDto) throws BusinessException {
        log.info("Edit user {}, new data {}.", requestDto.getEmail(), requestDto.toString());
        User userToEdit = findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());
        if (requestDto.getNewPassword() != null) {
            requestDto.setNewPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        }
        userRepository.editUser(requestDto);
    }

    public UserProfileResponse getUserProfile(String email) throws BusinessException {
        log.info("Get user profile {}", email);
        return userMapper.convertToProfile(userRepository.getUserByEmail(email).orElseThrow(
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
    public void resetPassword(ResetPasswordRequest resetDto) throws BusinessException {
        log.info("Starting to reset password. Token {}", resetDto.getToken());
        String token = resetDto.getToken();
        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            log.info("Trying to reset and edit password on {}", email);
            int success = userRepository.editPassword(
                    email,
                    passwordEncoder.encode(resetDto.getPassword()),
                    sha1Hex(token)
            );
            log.info("Reset and editing password {}", success > 0 ? "success" : "failed");
        } else {
            throw new BusinessException("Exception.resetPassword.tokenExpired");
        }
    }

    public void saveFcmUserToken(NotificationTokenRequest requestDto) {
        log.debug("Saving user FCM token, email {}", requestDto.getEmail());
        String userId = userRepository.getUserIdByEmail(requestDto.getEmail());
        MailingToken token = new MailingToken(userId, requestDto.getToken(), true);
        mailingTokenRepository.save(token);
    }

    @Transactional
    public void disableFcmUserToken(String email) {
        log.debug("Disabling user FCM token availability, email {}", email);
        String userId = userRepository.getUserIdByEmail(email);
        MailingToken token = entityManager.find(MailingToken.class, userId);
        token.setActive(false);
    }

    public List<UserTableRow> getUsersTable() {
        return userRepository.findAll().stream()
                .map(userMapper::convertToTableRow)
                .collect(Collectors.toList());
    }

    public Map<String, List<SimpleNameObj>> getDictsTable() {
        Map<String, List<SimpleNameObj>> dicts = new HashMap<>();
        List<SimpleNameObj> languages = dictRepository.getAllLanguage()
                .stream()
                .map(it -> new SimpleNameObj(it.getId(), it.getFullName()))
                .collect(Collectors.toList());
        dicts.put("languages", languages);
        return dicts;
    }

    public AuthResponse webLogin(AuthRequest authRequest) throws BusinessException {
        log.info("Login in web-admin with email {}", authRequest.getEmail());
        User user = findByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
        if (!ROLE_ADMIN.equals(user.getRole())) {
            throw new BusinessException("Недостаточно прав. Обратитесь к администратору.");
        }
        String token = jwtProvider.generateAccessToken(user.getEmail());
        return new AuthResponse(token, userMapper.convertToResponse(user));
    }
}
