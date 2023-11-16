package com.kdt_y_be_toy_project2.global.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kdt_y_be_toy_project2.global.security.formlogin.CustomFormLoginFilter;
import com.kdt_y_be_toy_project2.global.security.jwt.JwtAuthenticationFilter;

@Configuration
public class SecurityFilterConfig {

	private final CustomFormLoginFilter customFormLoginFilter;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityFilterConfig(
		CustomFormLoginFilter customFormLoginFilter,
		JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.customFormLoginFilter = customFormLoginFilter;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	SecurityFilterChain http(HttpSecurity http) throws Exception {
		http
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(sessionConfig ->
				sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(request ->
			request.requestMatchers("/v1/members/signUp", "/login", "/v1/refresh").permitAll()
				.requestMatchers(HttpMethod.GET, "/v1/trips/my/likes").authenticated()
				.requestMatchers(HttpMethod.GET, "/v1/trips", "/v1/trips/**").permitAll()
				.requestMatchers("/swagger-ui/**", "/swagger/**", "/swagger-resources/**", "/v3/**").permitAll()
				.anyRequest().authenticated()
		);

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(customFormLoginFilter, JwtAuthenticationFilter.class);

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
