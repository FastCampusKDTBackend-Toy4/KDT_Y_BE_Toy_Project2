package com.kdt_y_be_toy_project2.global.jwt.repository;

public record JwtEntity(

    String key,
    String value,
    long expiration
) {
}
