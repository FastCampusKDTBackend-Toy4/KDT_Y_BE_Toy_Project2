package com.kdt_y_be_toy_project2.global.factory;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kdt_y_be_toy_project2.domain.member.domain.Member;

public class MemberTestFactory {

	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public static Member createTestMemberWithRandomPassword() {
		return Member.builder()
			.name("멤버테스트" + ThreadLocalRandom.current().nextInt(1000000))
			.email("test" + ThreadLocalRandom.current().nextInt(1000000) + "@test.com")
			.password(passwordEncoder.encode(String.valueOf(ThreadLocalRandom.current().nextInt())))
			.build();
	}
}
