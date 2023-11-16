package com.kdt_y_be_toy_project2.domain.trip.dto;

import java.util.List;
import java.util.Map;

public record CommentResponse(List<Map<String, Object>> comments) {
}
