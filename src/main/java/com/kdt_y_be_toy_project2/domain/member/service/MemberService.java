package com.kdt_y_be_toy_project2.domain.member.service;

import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.member.dto.request.SignUpRequest;
import com.kdt_y_be_toy_project2.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(SignUpRequest request) {
        memberRepository.save(Member.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .name(request.name())
            .build());
    }
}
