package com.kdt_y_be_toy_project2.domain.member.dto.request;

public record LoginResponse(
    String email,
    String accessToken,
    String refreshToken
) {

}
