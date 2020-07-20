package ru.tpu.russian.back.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tpu.russian.back.Jwt.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityConfig(JwtConfigurer jwtConfigurer,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtConfigurer = jwtConfigurer;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    private final JwtConfigurer jwtConfigurer;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;



//    @Bean
//    public FilterRegistrationBean<JwtFilter> filterRegistrationBean() {
//        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(jwtFilter);
//        registrationBean.addUrlPatterns("/test/permissions");
//        return registrationBean;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/api/**", "/test", "/oauth2/callback/*", "/");
    }

    //    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .httpBasic().disable()
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/test/permissions").hasRole("USER")
//                .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
//    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/test/permissions").hasRole("USER")
                .and().apply(jwtConfigurer)
                .and()
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint());
    }
}
