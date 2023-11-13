package com.kdt_y_be_toy_project2.domain.member.service;

import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.member.dto.request.LoginRequest;
import com.kdt_y_be_toy_project2.domain.member.dto.request.LoginResponse;
import com.kdt_y_be_toy_project2.domain.member.dto.request.SignUpRequest;
import com.kdt_y_be_toy_project2.domain.member.repository.MemberRepository;
import com.kdt_y_be_toy_project2.global.jwt.JwtPayload;
import com.kdt_y_be_toy_project2.global.jwt.JwtProvider;
import java.util.Date;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
        JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public void signUp(SignUpRequest request) {
        memberRepository.save(Member.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .name(request.name())
            .build());
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findById(loginRequest.username())
            .orElseThrow(() -> new IllegalArgumentException("member not found"));

        if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new IllegalArgumentException("Not match password");
        }

        String accessToken = jwtProvider.createAccessToken(
            new JwtPayload(loginRequest.username(), new Date()));
        String refreshToken = jwtProvider.createRefreshToken(
            new JwtPayload(loginRequest.username(), new Date()));

        return new LoginResponse(loginRequest.username(), accessToken, refreshToken);
    }
}
