package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.SimpleNameObj;
import ru.tpu.russian.back.dto.auth.*;
import ru.tpu.russian.back.dto.notification.*;
import ru.tpu.russian.back.dto.user.*;
import ru.tpu.russian.back.dto.user.calendarEvent.*;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.entity.calendarEvent.*;
import ru.tpu.russian.back.entity.dict.StudyGroup;
import ru.tpu.russian.back.entity.notification.MailingToken;
import ru.tpu.russian.back.entity.security.*;
import ru.tpu.russian.back.enums.*;
import ru.tpu.russian.back.exception.*;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.mapper.UserMapper;
import ru.tpu.russian.back.repository.language.LanguageRepository;
import ru.tpu.russian.back.repository.notification.MailingTokenRepository;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static ru.tpu.russian.back.enums.ProviderType.valueOf;
import static ru.tpu.russian.back.service.MailService.TypeMessages.CONFIRMATION_MESSAGE;
import static ru.tpu.russian.back.service.MailService.TypeMessages.RESET_PASSWORD_MESSAGE;

@Service
@Slf4j
public class UserService {

    private static final int HTTP_STATUS_REG_NEED_FILL = 210;

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private static final String TITLE_NOTIFICATION_CALENDAR_EVENT = "Вам назначено новое событие!";

    private static final String MESSAGE_NOTIFICATION_CALENDAR_EVENT = "%s\r\n%s\r\nЗагляните в календарь...";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final MailService mailService;

    private final MailingTokenRepository mailingTokenRepository;

    private final UserMapper userMapper;

    private final LanguageRepository languageRepository;

    private final NotificationService notificationService;

    @Value("${firebase.group-all-users}")
    private String groupAllUsers;

    @PersistenceContext
    private EntityManager entityManager;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            MailService mailService,
            MailingTokenRepository mailingTokenRepository,
            LanguageRepository languageRepository,
            UserMapper userMapper,
            NotificationService notificationService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.mailService = mailService;
        this.mailingTokenRepository = mailingTokenRepository;
        this.userMapper = userMapper;
        this.languageRepository = languageRepository;
        this.notificationService = notificationService;
    }

    public void register(UserRegisterRequest registrationRequestDto) {
        log.info("Register new user. {}", registrationRequestDto.toString());
        log.debug("Check registration parameters on valid.");
        checkEmailOnExist(registrationRequestDto.getEmail());
        registrationRequestDto.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        log.debug("Saving new user in DB.");
        userRepository.saveUser(registrationRequestDto);
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
            log.error("This email address {} already taken.", email);
            throw new BusinessException("Exception.registration.email.exist", email);
        }
    }

    public AuthResponse login(AuthRequest authRequest) throws BusinessException {
        log.info("Login in system with email {}", authRequest.getEmail());
        User user = findByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
        String token = jwtProvider.generateAccessToken(user.getEmail());
        if (authRequest.isRememberMe()) {
            log.debug("Option rememberMe selected. Generating refresh token.");
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
                log.debug("The passwords matched. Email {}", email);
                return user;
            } else {
                log.error("Password mismatch");
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
                    throw new BusinessException(
                            "Exception.login.service.wrongService",
                            authRequest.getProvider(), user.getProvider()
                    );
                }
            }
            String token = jwtProvider.generateAccessToken(user.getEmail());
            String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
            AuthResponse response = new AuthResponse(token, refreshToken, userMapper.convertToResponse(user));
            return new ResponseEntity<>(
                    response, HttpStatus.OK
            );
        } else {
            log.info("This user does not exist in the database. Registering.");

            /**
             * Отправляем обратно мобильному приложению данные пользователя (здесь уже есть те, что мы смогли вытащить из API соц.сети)
             * Пользователю необходимо дозаполнить
             */
            return ResponseEntity.status(HTTP_STATUS_REG_NEED_FILL)
                    .headers(new HttpHeaders())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userInfo);
        }
    }

    public AuthResponse registerWithService(UserRegisterRequest registrationRequest) throws BusinessException {
        log.info("Register new user through service. {}", registrationRequest.toString());
        checkEmailOnExist(registrationRequest.getEmail());
        registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        log.debug("Saving new user in DB.");
        User user = userRepository.saveUser(registrationRequest);
        String token = jwtProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
        return new AuthResponse(token, refreshToken, userMapper.convertToResponse(user));
    }

    public void editUserProfile(UserProfileEditRequest requestDto) {
        log.info("Edit user {}, new data {}.", requestDto.getEmail(), requestDto.toString());
        if (requestDto.getNewPassword() != null) {
            if (requestDto.getPassword() != null) {
                findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());
                requestDto.setNewPassword(passwordEncoder.encode(requestDto.getNewPassword()));
            } else {
                throw new BusinessException("Exception.user.currentPassword.notFound");
            }
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
        if (token != null) {
            token.setActive(false);
        } else {
            log.warn("Trying to deactivate token which doesn't exist, email {}", email);
        }
    }

    public List<UserTableRow> getUsersTable() {
        return userRepository.findAll().stream()
                .map(userMapper::convertToTableRow)
                .collect(Collectors.toList());
    }

    public Map<String, List<SimpleNameObj>> getDictsTable() {
        Map<String, List<SimpleNameObj>> dicts = new HashMap<>();
        List<SimpleNameObj> languages = languageRepository.findAll()
                .stream()
                .map(it -> new SimpleNameObj(it.getId(), it.getName()))
                .collect(Collectors.toList());
        dicts.put("languages", languages);
        return dicts;
    }

    public AuthResponse webLogin(AuthRequest authRequest) throws BusinessException {
        log.debug("Login in web-admin with email {}", authRequest.getEmail());
        User user = findByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
        if (!ROLE_ADMIN.equals(user.getRole())) {
            throw new BusinessException("Недостаточно прав. Обратитесь к администратору.");
        }
        String token = jwtProvider.generateAccessToken(user.getEmail());
        return new AuthResponse(token, userMapper.convertToResponse(user));
    }

    @Transactional
    public void createCalendarEvent(CalendarEventCreateRequest request, String token) {
        CalendarEvent event = userMapper.convertToCalendarEventFromRequest(request);
        switch (event.getTargetEnum()) {
            case STUDY_GROUP:
                event.addNewTargets(request.getGroups()
                        .stream()
                        .map(it -> new CalendarEventTargets(event, entityManager.getReference(StudyGroup.class, it)))
                        .collect(Collectors.toSet()));
                break;
            case SELECTED_USERS:
                event.addNewTargets(request.getSelectedUsers()
                        .stream()
                        .map(it -> new CalendarEventTargets(event, entityManager.getReference(User.class, it)))
                        .collect(Collectors.toSet()));
                break;
        }
        if (request.isSendNotification()) {
            sendNotificationAboutCreatedEvent(request, token);
        }
        entityManager.persist(event);
    }

    private void sendNotificationAboutCreatedEvent(CalendarEventCreateRequest request, String token) {
        NotificationBaseRequest requestNotification = createNotificationRequestFromTarget(
                request.getGroupTarget(),
                request,
                token
        );
        if (requestNotification instanceof NotificationRequestUsers) {
            notificationService.sendOnUser((NotificationRequestUsers)requestNotification);
        } else {
            notificationService.sendOnGroup((NotificationRequestGroup)requestNotification);
        }
    }

    private NotificationBaseRequest createNotificationRequestFromTarget(
            CalendarEventGroupTarget target,
            CalendarEventCreateRequest request,
            String token
    ) {
        NotificationBaseRequest requestNotification;
        switch (target) {
            case ALL:
                requestNotification = new NotificationRequestGroup();
                ((NotificationRequestGroup)requestNotification).setTargetGroupName(groupAllUsers);
                break;
            case STUDY_GROUP:
                requestNotification = new NotificationRequestUsers();
                ((NotificationRequestUsers)requestNotification).setUsers(request.getGroups()
                        .stream()
                        .map(userRepository::getUsersByGroupId)
                        .collect(Collectors.toList())
                        .stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList()));
                break;
            case SELECTED_USERS:
                requestNotification = new NotificationRequestUsers();
                ((NotificationRequestUsers)requestNotification).setUsers(request.getSelectedUsers());
                break;
            default:
                throw new IllegalArgumentException("Wrong target calendar event value " + target.toString());
        }
        requestNotification.setTitle(TITLE_NOTIFICATION_CALENDAR_EVENT);
        requestNotification.setMessage(String.format(
                MESSAGE_NOTIFICATION_CALENDAR_EVENT,
                request.getTitle(), request.getDescription()
        ));
        requestNotification.setAdminEmail(jwtProvider.getEmailFromToken(jwtProvider.unwrapTokenFromHeaderStr(token)));
        return requestNotification;
    }

    public List<CalendarEventResponse> getCalendarEvents(String token) {
        String email = jwtProvider.getEmailFromToken(jwtProvider.unwrapTokenFromHeaderStr(token));
        if (email != null) {
            return userRepository.getCalendarEventsByEmail(email)
                    .stream()
                    .map(userMapper::convertCalendarEventToResponse)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Email in token is empty.");
        }
    }

    /**
     * Удалить пользователя
     */
    public void deleteUser(String id) {
        log.info("Delete user with ID = {}", id);
        userRepository.deleteUserById(id);
    }

    /**
     * Создать пользователя
     */
    public void createUser(UserRegisterRequest request) throws AttrValidationErrorException {
        log.info("Create new user from admin-panel. {}", request.toString());
        checkEmailExistForAdminPanel(request.getEmail());
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.saveUser(request);
    }

    private void checkEmailExistForAdminPanel(String email) {
        Optional<User> user = userRepository.getUserByEmail(email);
        if (user.isPresent()) {
            throw new AttrValidationErrorException(
                    singletonList(new AttrValidationError("email", "Данный адрес занят")));
        }
    }

    /**
     * Меняем профиль пользователя из админ-панели
     */
    public void editUserFromAdminPanel(String id, UserProfileEditRequest request) {
        log.info("Edit user ID {} from admin-panel. {}", id, request.toString());
        User targetUser = userRepository.findById(id).orElse(null);
        if (targetUser != null) {
            if (!targetUser.getEmail().equals(request.getEmail())) {
                checkEmailExistForAdminPanel(request.getEmail());
            }
            userRepository.editUserByAdmin(id, request);
        } else {
            throw new BusinessException("Редактируемый пользователь не найден");
        }
    }
}
