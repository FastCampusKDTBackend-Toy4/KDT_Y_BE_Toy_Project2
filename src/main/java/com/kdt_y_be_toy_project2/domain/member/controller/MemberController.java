package com.kdt_y_be_toy_project2.domain.member.controller;

import com.kdt_y_be_toy_project2.domain.member.dto.request.LoginRequest;
import com.kdt_y_be_toy_project2.domain.member.dto.request.LoginResponse;
import com.kdt_y_be_toy_project2.domain.member.dto.request.SignUpRequest;
import com.kdt_y_be_toy_project2.domain.member.service.MemberService;
import com.kdt_y_be_toy_project2.global.config.CustomHttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signUp")
    public void join(SignUpRequest request) {
        memberService.signUp(request);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(LoginRequest loginRequest) {
        LoginResponse loginResponse = memberService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.OK)
            .header(CustomHttpHeaders.ACCESS_TOKEN, loginResponse.accessToken())
            .header(CustomHttpHeaders.REFRESH_TOKEN, loginResponse.refreshToken())
            .build();
    }
}
