package com.kdt_y_be_toy_project2.global.security.jwt;

import java.util.Objects;
import java.util.Set;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String principal;
    private String credentials;
    private final boolean isAuthenticated;

    private JwtAuthenticationToken(String principal, String credentials, boolean isAuthenticated) {
        super(Set.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.principal = principal;
        this.credentials = credentials;
        this.isAuthenticated = isAuthenticated;
    }

    private JwtAuthenticationToken(String principal, boolean isAuthenticated) {
        super(Set.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.principal = principal;
        this.isAuthenticated = isAuthenticated;
    }

    public static JwtAuthenticationToken unAuthorize(String accessToken) {
        return new JwtAuthenticationToken(null, accessToken, false);
    }

    public static JwtAuthenticationToken authorize(String email) {
        return new JwtAuthenticationToken(email, true);
    }

    @Override
    public String getPrincipal() {
        return principal;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        JwtAuthenticationToken that = (JwtAuthenticationToken) object;
        return isAuthenticated == that.isAuthenticated && Objects.equals(principal,
            that.principal) && Objects.equals(credentials, that.credentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), principal, credentials, isAuthenticated);
    }
}
