package ru.tpu.russian.back.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tpu.russian.back.repository.user.UserRepository;

import java.security.SecureRandom;
import java.time.*;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    private final UserRepository userRepository;

    public JwtProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final long DAYS_EXP_ACCESS_TOKEN = 7L;

    private static final long DAYS_EXP_REFRESH_TOKEN = 15L;

    private static SecureRandom random = new SecureRandom();

    private static final String ALL_SYMBOLS_TO_GENERATE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz/*&^$@!";

    @Value("$(jwt.secret)")
    private String jwtSecret;

    public String generateToken(String email) {
        log.info("Starting generate access token, email {}, expiration time {}", email, DAYS_EXP_ACCESS_TOKEN);
        Date date = Date.from(LocalDate.now().plusDays(DAYS_EXP_ACCESS_TOKEN).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .claim("salt", generateRandomSalt())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateRefreshToken(String email) {
        log.info("Starting generate refresh token, email {}, expiration time {}", email, DAYS_EXP_REFRESH_TOKEN);
        Date date = Date.from(LocalDate.now().plusDays(DAYS_EXP_REFRESH_TOKEN).atStartOfDay(ZoneId.systemDefault()).toInstant());
        String refreshSalt = generateRandomSalt();
        userRepository.editRefreshSalt(email, refreshSalt);
        return Jwts.builder()
                .setSubject(email)
                .claim("refresh_salt", refreshSalt)
                .claim("salt", generateRandomSalt())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired");
            throw expEx;
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt");
        } catch (SignatureException sEx) {
            log.error("Invalid signature");
        } catch (Exception e) {
            log.error("Invalid token");
        }
        return false;
    }

    public static String generateRandomSalt() {
        StringBuilder sb = new StringBuilder();
        int lengthSalt = random.nextInt(15 - 10) + 10;
        for (int i = 0; i < lengthSalt; i++) {
            sb.append(ALL_SYMBOLS_TO_GENERATE.charAt(random.nextInt(ALL_SYMBOLS_TO_GENERATE.length())));
        }
        return sb.toString();
    }

    public String getEmailFromToken(String token) {
        log.info("Getting email from token...");
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getSaltFromRefreshToken(String token) {
        log.info("Getting salt from token...");
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return (String) claims.get("refresh_salt");
    }
}
