package ru.tpu.russian.back.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tpu.russian.back.jwt.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int STRENGTH_BCRYPT = 10;

    public SecurityConfig(JwtConfigurer jwtConfigurer,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        super();
        this.jwtConfigurer = jwtConfigurer;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    private final JwtConfigurer jwtConfigurer;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH_BCRYPT);
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                // Отключаем срабатывание фильтра на некоторые запросы
                .antMatchers("/api/auth/**", "/api/token/**", "/api/email/confirmation",
                        "/", "/test/**", "/api/media/img/*", "/api/dict/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/**/admin/**").hasRole("ADMIN")
                .antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
                .and().apply(jwtConfigurer)
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }
}
