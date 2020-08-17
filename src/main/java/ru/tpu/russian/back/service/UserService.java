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
import ru.tpu.russian.back.exception.InternalException;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
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

    public void register(BaseUserRequestDto registrationRequestDto) throws InternalException {
        log.info("Register new user. {}", registrationRequestDto.toString());
        log.debug("Check registration parameters on valid.");
        checkValidEmail(registrationRequestDto.getEmail());
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

    private void checkValidEmail(String email) throws InternalException {
        log.debug("Check email address for availability in the database.");
        Optional<User> userOptional = userRepository.getUserByEmail(email);
        if (userOptional.isPresent()) {
            log.warn("This email address already taken.");
            throw new InternalException("Exception.reg.email.exist", email);
        }
    }

    private User convertRegRequestToUser(BaseUserRequestDto request) {
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

    private Map<String, Object> putUserFieldToRegMap(User user) {
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

    public AuthResponseDto login(AuthRequestDto authRequest) throws InternalException {
        log.info("Login in system with email {}", authRequest.getEmail());
        User user = findByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
        String token = jwtProvider.generateToken(user.getEmail());
        if (authRequest.isRememberMe()) {
            log.debug("Option rememberMe selected.");
            String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
            return new AuthResponseDto(token, refreshToken, true, new UserResponseDto(user));
        }
        return new AuthResponseDto(token, true, new UserResponseDto(user));
    }

    private User findByEmailAndPassword(String email, String password) throws InternalException {
        log.debug("Try to find user with email {} in DB", email);
        User user = findByEmail(email);
        if (user != null) {
            log.debug("User founded. Compare password.");
            if (passwordEncoder.matches(password, user.getPassword())) {
                log.info("The passwords matched. Email {}", email);
                return user;
            } else {
                log.warn("Password mismatch");
                throw new InternalException("Exception.login.mismatch");
            }
        } else {
            log.error("User is not found.");
            throw new InternalException("Exception.login.notFound", email);
        }
    }

    private User findByEmail(String email) {
        Optional<User> user = userRepository.getUserByEmail(email);
        return user.orElse(null);
    }

    public ResponseEntity<?> loginWithService(AuthRequestWithServiceDto authRequest) throws InternalException {
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
                throw new InternalException("Exception.login.service.wrongService",
                        authRequest.getProvider(), user.getProvider());
            }
            String token = jwtProvider.generateToken(user.getEmail());
            String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
            AuthResponseDto response = new AuthResponseDto(token, refreshToken, true, new UserResponseDto(user));
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

    public void registerWithService(BaseUserRequestDto registrationRequest) throws InternalException {
        log.info("Register new user through service. User {}", registrationRequest.toString());
        log.debug("Convert to entity User.");
        checkValidEmail(registrationRequest.getEmail());
        User user = convertRegRequestToUser(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setConfirm(true);
        log.debug("Saving new user in DB.");
        register(user);
    }

    public AuthResponseDto refreshToken(HttpServletRequest servletRequest) throws InternalException {
        log.info("Refresh access token");
        String token = jwtProvider.getTokenFromRequest(servletRequest);
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
            } else {
                log.error("The secret of the refresh token did not match.");
                throw new InternalException("Exception.login.refreshToken.mismatch");
            }
        } else {
            log.error("The token was not found in the request headers.");
            throw new InternalException("Exception.login.token.notFoundOrInvalid");
        }
    }

    public boolean checkAuth(CheckAuthRequestDto requestDto) {
        log.info("Checking user on authenticated.");
        String token = requestDto.getToken();
        if (token != null && jwtProvider.validateToken(token)) {
            log.debug("Compare email in request with email in token.");
            String email = jwtProvider.getEmailFromToken(token);
            return requestDto.getEmail().equals(email);
        }
        log.warn("User not authenticated.");
        return false;
    }

    public void editUser(BaseUserRequestDto requestDto) throws InternalException {
        User userToEdit = findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());
        Map<String, Object> paramsToProcedure = putEditedUserFieldToMap(requestDto);
        userRepository.editUser(paramsToProcedure);
    }

    private Map<String, Object> putEditedUserFieldToMap(BaseUserRequestDto requestDto) {
        Map<String, Object> paramsToProcedure = new HashMap<>();
        paramsToProcedure.put("psw", passwordEncoder.encode(requestDto.getNewPassword()));
        paramsToProcedure.put("firstName", requestDto.getFirstName());
        paramsToProcedure.put("lang", requestDto.getLanguage().toString());
        paramsToProcedure.put("secondName", requestDto.getLastName());
        paramsToProcedure.put("patronymic", requestDto.getMiddleName());
        paramsToProcedure.put("sex", requestDto.getGender());
        paramsToProcedure.put("phoneNum", requestDto.getPhoneNumber());
        return paramsToProcedure;
    }

    public UserResponseDto getUserProfile(String email) throws InternalException {
        return new UserResponseDto(userRepository.getUserByEmail(email).orElseThrow(
                () -> new InternalException("Exception.login.notFound", email)));
    }

//    public List<User> getAllByLanguage(String language) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("Language", language);
//        return userRepository.getAllByLanguage(params);
//    }
//
//    public List<User> getAllByReg(boolean reg) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("Reg", reg);
//        return userRepository.getAllByReg(params);
//    }
}
