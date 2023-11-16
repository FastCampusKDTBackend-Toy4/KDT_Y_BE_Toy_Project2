package com.kdt_y_be_toy_project2.global.jwt.service;

public record TokenPair(
    String accessToken,
    String refreshToken
) {

}
