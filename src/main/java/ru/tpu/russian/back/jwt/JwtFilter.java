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

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

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
        String token = jwtProvider.getTokenFromRequest(request);
        try {
            if (token != null && jwtProvider.validateToken(token)) {
                log.debug("Token in request - {}", token);
                String userEmail = jwtProvider.getEmailFromToken(token);
                log.debug("Email in token: {}", userEmail);
                log.debug("Try to find user in DB.");
                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userEmail);
                log.info("User founded. Auth success.");
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        customUserDetails,
                        null,
                        customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.error("Token does not exist in request or not valid.");
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            log.error("Token {} expired", token);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired.");
        }
    }
}
