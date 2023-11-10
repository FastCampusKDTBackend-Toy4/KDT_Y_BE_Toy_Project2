package com.kdt_y_be_toy_project2.global.jwt;

import java.util.Date;

public record JwtPayload(
    String email,
    Date issuedAt
) {

}
