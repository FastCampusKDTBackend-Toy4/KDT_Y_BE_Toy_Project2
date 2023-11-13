package com.kdt_y_be_toy_project2.global.security.login;

import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class AccountContext extends User {

    private final Member member;

    public AccountContext(Member member) {
        super(member.getEmail(), member.getPassword(), Collections.emptySet());
        this.member = member;
    }

    public AccountContext(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getEmail(), null, authorities);
        this.member = member;
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
        AccountContext that = (AccountContext) object;
        return Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), member);
    }
}
