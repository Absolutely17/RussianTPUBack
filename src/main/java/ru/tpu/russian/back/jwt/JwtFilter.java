package ru.tpu.russian.back.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.tpu.russian.back.entity.security.CustomUserDetails;
import ru.tpu.russian.back.service.security.CustomUserDetailsService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;

    private final CustomUserDetailsService customUserDetailsService;

    public JwtFilter(
            JwtProvider jwtProvider,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            String token = getTokenFromRequest(request);
            if (token != null && jwtProvider.validateToken(token)) {
                log.info("Token in request - {}", token);
                String userEmail = jwtProvider.getEmailFromToken(token);
                log.info("Email in token: {}", userEmail);
                log.info("Try to find user in DB.");
                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userEmail);
                log.info("User founded. Auth success.");
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.error("Token does not exist in request or not valid.");
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired.");
        }
    }

    public static String getTokenFromRequest(HttpServletRequest servletRequest) {
        log.info("Getting token from request...");
        String bearer = servletRequest.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer")) {
            return bearer.substring(7);
        } else {
            log.error("Could not find a token in the header.");
            return null;
        }
    }
}
