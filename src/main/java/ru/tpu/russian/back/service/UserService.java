package ru.tpu.russian.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.enums.ProviderType;
import ru.tpu.russian.back.dto.request.*;
import ru.tpu.russian.back.dto.response.*;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.entity.security.*;
import ru.tpu.russian.back.exception.RegistrationException;
import ru.tpu.russian.back.jwt.JwtProvider;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static ru.tpu.russian.back.service.security.AuthConst.HTTP_STATUS_REG_NEED_FILL;

@Service
@Slf4j
public class UserService {

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

    public void confirmRegistration(String token) {
        log.debug("Check input token on valid.");
        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            int isSuccess = userRepository.editRegisteredStatus(email, true);
            log.debug("Confirm email {}", isSuccess > 0 ? "success" : "failed");
        }
    }

    public void register(RegistrationRequestDto registrationRequestDto) throws RegistrationException {
        log.info("Register new user. {}", registrationRequestDto.toString());
        log.info("Check registration parameters on valid.");
        checkValidEmail(registrationRequestDto.getEmail());
        User user = convertRegRequestToUser(registrationRequestDto);
        user.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        log.info("Saving new user in DB...");
        register(user);
        mailService.sendMessage(registrationRequestDto.getEmail(), registrationRequestDto.getFirstName());
    }

    private void checkValidEmail(String email) throws RegistrationException {
        log.info("Check email address for availability in the database.");
        Optional<User> userOptional = userRepository.getUserByEmail(email);
        if (userOptional.isPresent()) {
            log.error("This email address already taken.");
            throw new RegistrationException("Email address already taken.");
        }
    }

    private User convertRegRequestToUser(RegistrationRequestDto request) {
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
        Map<String, Object> params = putUserFieldToMap(user);
        userRepository.saveUser(params);
    }

    private Map<String, Object> putUserFieldToMap(User user) {
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

    public AuthResponseDto login(AuthRequestDto authRequest) throws LoginException {
        log.info("Login in system with email {}", authRequest.getEmail());
        User user = findByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
        String token = jwtProvider.generateToken(user.getEmail());
        if (authRequest.isRememberMe()) {
            log.info("Option rememberMe selected.");
            String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
            return new AuthResponseDto(token, refreshToken, true, new UserResponseDto(user));
        }
        return new AuthResponseDto(token, true, new UserResponseDto(user));
    }

    private User findByEmailAndPassword(String email, String password) throws LoginException {
        log.info("Try to find user with email {} in DB", email);
        User user = findByEmail(email);
        if (user != null) {
            log.info("User founded. Compare password...");
            if (passwordEncoder.matches(password, user.getPassword())) {
                log.info("The passwords matched. Email {}", email);
                return user;
            } else {
                log.error("Password mismatch");
                throw new LoginException("Password mismatch.");
            }
        } else {
            log.error("User is not found.");
            throw new LoginException("User is not found.");
        }
    }

    private User findByEmail(String email) {
        Optional<User> user = userRepository.getUserByEmail(email);
        return user.orElse(null);
    }

    public ResponseEntity<?> loginWithService(AuthRequestWithServiceDto authRequest) {
        log.info("Login in system with service {}", authRequest.getProvider());
        try {
            OAuthUserInfo userInfo = OAuthServiceUserInfoFactory.getOAuthUserInfo(
                    authRequest.getProvider(),
                    authRequest.getToken(),
                    authRequest.getUserId(),
                    authRequest.getEmail()
            );
            User user = findByEmail(userInfo.getEmail());
            if (user != null) {
                if (!user.getProvider().equals(ProviderType.valueOf(authRequest.getProvider()))) {
                    throw new LoginException("It looks like you are trying to log in from the wrong provider." +
                            " Are you trying to log in through " + authRequest.getProvider() + "," +
                            " but you need to enter through " + user.getProvider());
                }
                String token = jwtProvider.generateToken(user.getEmail());
                String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
                AuthResponseDto response = new AuthResponseDto(token, refreshToken, true, new UserResponseDto(user));
                return new ResponseEntity<>(
                        response, HttpStatus.OK
                );
            } else {
                log.info("This user is not in the database. We register.");
                return ResponseEntity.status(HTTP_STATUS_REG_NEED_FILL)
                        .headers(new HttpHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userInfo);
            }
        } catch (LoginException ex) {
            return new ResponseEntity<>(
                    ex.getMessage(), UNAUTHORIZED
            );
        }
    }

    public void registerWithService(RegistrationRequestDto registrationRequest) throws RegistrationException {
        log.info("Register new user through service. User {}", registrationRequest.toString());
        log.info("Convert to entity User.");
        checkValidEmail(registrationRequest.getEmail());
        User user = convertRegRequestToUser(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setConfirm(true);
        log.info("Saving new user in DB.");
        register(user);
    }

    public AuthResponseDto refreshToken(HttpServletRequest servletRequest) throws LoginException {
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
                throw new LoginException("The secret of the refresh token did not match.");
            }
        } else {
            log.error("The token was not found in the request headers.");
            throw new LoginException("The token was not found in the request headers or not valid.");
        }
    }

    public boolean checkAuth(CheckAuthRequestDto requestDto) {
        log.info("Checking user on authenticated.");
        String token = requestDto.getToken();
        if (token != null && jwtProvider.validateToken(token)) {
            log.info("Compare email in request with email in token.");
            String email = jwtProvider.getEmailFromToken(token);
            return requestDto.getEmail().equals(email);
        }
        log.error("User not authenticated.");
        return false;
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
