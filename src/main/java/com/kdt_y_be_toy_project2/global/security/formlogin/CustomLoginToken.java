package com.kdt_y_be_toy_project2.global.security.formlogin;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomLoginToken extends AbstractAuthenticationToken {

    private final AccountContext accountContext;

    private CustomLoginToken(AccountContext accountContext,
        Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.accountContext = accountContext;
    }

    public static CustomLoginToken unAuthenticate(String username, String password) {
        return new CustomLoginToken(new AccountContext(username, password, Collections.emptySet()), Collections.emptySet());
    }

    public static CustomLoginToken authenticate(String username,
        Collection<GrantedAuthority> authorities) {
        return new CustomLoginToken(new AccountContext(username, null, authorities), authorities);
    }

    @Override
    public Object getPrincipal() {
        return accountContext.getUsername();
    }

    @Override
    public Object getCredentials() {
        return accountContext.getPassword();
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
        CustomLoginToken that = (CustomLoginToken) object;
        return Objects.equals(accountContext, that.accountContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accountContext);
    }
}
