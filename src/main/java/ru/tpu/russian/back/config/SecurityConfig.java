package ru.tpu.russian.back.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.*;
import ru.tpu.russian.back.Jwt.*;
import ru.tpu.russian.back.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import ru.tpu.russian.back.service.security.*;
import ru.tpu.russian.back.utils.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private SocialLoginConfig socialLoginConfig;

    public SecurityConfig(SocialLoginConfig socialLoginConfig,
                          JwtConfigurer jwtConfigurer,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          CustomSuccessHandler customSuccessHandler,
                          CustomFailureHandler customFailureHandler,
                          HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.socialLoginConfig = socialLoginConfig;
        this.jwtConfigurer = jwtConfigurer;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.customSuccessHandler = customSuccessHandler;
        this.customFailureHandler = customFailureHandler;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    private final JwtConfigurer jwtConfigurer;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final CustomSuccessHandler customSuccessHandler;

    @Autowired
    public void setCustomOAuth2UserService(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    private CustomOAuth2UserService customOAuth2UserService;

    private final CustomFailureHandler customFailureHandler;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

//    @Bean
//    public FilterRegistrationBean<JwtFilter> filterRegistrationBean() {
//        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(jwtFilter);
//        registrationBean.addUrlPatterns("/test/permissions");
//        return registrationBean;
//    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {

        return new InMemoryOAuth2AuthorizedClientService(
                socialLoginConfig.clientRegistrationRepository());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
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
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and()
                .oauth2Login()
                .clientRegistrationRepository(socialLoginConfig.clientRegistrationRepository())
                .authorizedClientService(authorizedClientService())
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(customSuccessHandler)
                .failureHandler(customFailureHandler);
    }
}
