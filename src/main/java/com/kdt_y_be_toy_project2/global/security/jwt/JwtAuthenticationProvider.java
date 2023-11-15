package com.kdt_y_be_toy_project2.global.security.jwt;

import com.kdt_y_be_toy_project2.global.jwt.JwtPayload;
import com.kdt_y_be_toy_project2.global.jwt.service.JwtService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    public JwtAuthenticationProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        String accessToken = (String) authentication.getCredentials();
        JwtPayload jwtPayload = jwtService.verifyToken(accessToken);

        return JwtAuthenticationToken.authorizeToken(jwtPayload.email());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
