package com.kdt_y_be_toy_project2.global.security.jwt;

import java.util.Set;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String principal;
    private JwtPair credentials;
    private final boolean isAuthenticated;

    private JwtAuthenticationToken(String accessToken, String refreshToken, boolean isAuthenticated) {
        super(Set.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.credentials = new JwtPair(accessToken, refreshToken);
        this.isAuthenticated = isAuthenticated;
    }

    private JwtAuthenticationToken(String email, boolean isAuthenticated) {
        super(Set.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.principal = email;
        this.isAuthenticated = isAuthenticated;
    }

    public static JwtAuthenticationToken emptyToken() {
        return new JwtAuthenticationToken(null, null, false);
    }

    public static JwtAuthenticationToken unAuthorizeToken(String accessToken, String refreshToken) {
        return new JwtAuthenticationToken(accessToken, refreshToken, false);
    }

    public static JwtAuthenticationToken authorizeToken(String email) {
        return new JwtAuthenticationToken(email, true);
    }

    @Override
    public String getPrincipal() {
        return principal;
    }

    @Override
    public JwtPair getCredentials() {
        return credentials;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
