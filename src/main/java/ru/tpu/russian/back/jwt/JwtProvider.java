package ru.tpu.russian.back.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.*;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static org.springframework.util.StringUtils.hasText;

@Component
@Slf4j
public class JwtProvider {

    private static final SecureRandom random = new SecureRandom();

    private static final String ALL_SYMBOLS_TO_GENERATE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz/*&^$@!";

    private static final String AUTHORIZATION = "Authorization";

    private final UserRepository userRepository;

    public JwtProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("$(jwt.secret)")
    private String jwtSecret;

    @Value("${jwt.expiration.access.token:7}")
    private long expAccessToken;

    @Value("${jwt.expiration.refresh.token:15}")
    private long expRefreshToken;

    public String generateResetPasswordToken(String email) {
        DateTimeZone zone = DateTimeZone.getDefault();
        DateTime date = new DateTime().withZone(zone).plusHours(1);
        String token = Jwts.builder()
                .setSubject(email)
                .setExpiration(date.toDate())
                .signWith(HS512, jwtSecret)
                .compact();
        return token;
    }

    public String generateTokenWithExpiration(String email, long expirationDays) {
        Date date = Date.from(LocalDate.now().plusDays(expirationDays)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .signWith(HS512, jwtSecret)
                .compact();
    }

    public String generateAccessToken(String email) {
        Date date = Date.from(LocalDate.now().plusDays(expAccessToken)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .claim("salt", generateRandomSalt())
                .signWith(HS512, jwtSecret)
                .compact();
    }

    public String generateRefreshToken(String email) {
        Date date = Date.from(LocalDate.now().plusDays(expRefreshToken)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        String refreshSalt = generateRandomSalt();
        userRepository.editRefreshSalt(email, refreshSalt);
        return Jwts.builder()
                .setSubject(email)
                .claim("refresh_salt", refreshSalt)
                .claim("salt", generateRandomSalt())
                .setExpiration(date)
                .signWith(HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired");
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

    private static String generateRandomSalt() {
        StringBuilder sb = new StringBuilder();
        int lengthSalt = random.nextInt(15 - 10) + 10;
        for (int i = 0; i < lengthSalt; i++) {
            sb.append(ALL_SYMBOLS_TO_GENERATE.charAt(random.nextInt(ALL_SYMBOLS_TO_GENERATE.length())));
        }
        return sb.toString();
    }

    @Nullable
    public String getTokenFromRequest(HttpServletRequest servletRequest) {
        String bearer = servletRequest.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer")) {
            return bearer.substring(7);
        } else {
            log.error("Could not find a token in the header.");
            return null;
        }
    }

    @Nullable
    public String unwrapTokenFromHeaderStr(String headerStr) {
        if (hasText(headerStr) && headerStr.startsWith("Bearer")) {
            return headerStr.substring(7);
        } else {
            log.error("Could not find a token in the header.");
            return null;
        }
    }

    @Nullable
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (Exception ex) {
            log.error("Problem with unwrapping email from token.", ex);
            return null;
        }
    }

    public String getSaltFromRefreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return (String) claims.get("refresh_salt");
    }
}
