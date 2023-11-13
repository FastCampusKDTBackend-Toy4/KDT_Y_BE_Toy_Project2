package com.kdt_y_be_toy_project2.global.security.jwt;

public record JwtPair(
    String accessToken,
    String refreshToken
) {

}
