package com.kdt_y_be_toy_project2.global.security;

import com.kdt_y_be_toy_project2.global.security.jwt.JwtAuthenticationFilter;
import com.kdt_y_be_toy_project2.global.security.login.CustomUsernamePasswordAuthenticationSuccessHandler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterConfig {

    private final CustomUsernamePasswordAuthenticationSuccessHandler customUsernamePasswordAuthenticationSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityFilterConfig(
        CustomUsernamePasswordAuthenticationSuccessHandler customUsernamePasswordAuthenticationSuccessHandler,
        JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUsernamePasswordAuthenticationSuccessHandler = customUsernamePasswordAuthenticationSuccessHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    SecurityFilterChain http(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionConfig ->
                sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(request ->
            request.requestMatchers("/v1/members/signUp", "/login").permitAll()
                .anyRequest().authenticated());

        http.formLogin(formLogin ->
            formLogin.successHandler(customUsernamePasswordAuthenticationSuccessHandler));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web ->
            web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/favicon.ico", "/resources/**", "/error");
    }
}