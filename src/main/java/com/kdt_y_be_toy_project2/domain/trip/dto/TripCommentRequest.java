package com.kdt_y_be_toy_project2.domain.trip.dto;

import jakarta.validation.constraints.NotBlank;

public record TripCommentRequest(@NotBlank(message = "댓글을 입력해주세요.") String content) {
}
