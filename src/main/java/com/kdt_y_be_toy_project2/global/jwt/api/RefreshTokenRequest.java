package com.kdt_y_be_toy_project2.global.jwt.api;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
    @NotBlank String accessToken,
    @NotBlank String refreshToken
) {

}
