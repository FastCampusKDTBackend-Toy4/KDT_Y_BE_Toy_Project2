package com.kdt_y_be_toy_project2.global.security.jwt;

import com.kdt_y_be_toy_project2.global.jwt.JwtPayload;
import com.kdt_y_be_toy_project2.global.jwt.JwtProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        JwtPair principal = (JwtPair) authentication.getCredentials();
        JwtPayload jwtPayload = jwtProvider.verifyToken(principal.accessToken());

        return JwtAuthenticationToken.authorizeToken(jwtPayload.email());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
