package com.kdt_y_be_toy_project2.domain.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(of = "email")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    private String email;

    private String password;

    private String name;

    @Builder
    private Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
