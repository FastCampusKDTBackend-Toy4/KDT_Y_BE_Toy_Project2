package com.kdt_y_be_toy_project2.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
    @Email @NotBlank String email,
    @NotBlank String password,
    @NotBlank String name
) {

}
