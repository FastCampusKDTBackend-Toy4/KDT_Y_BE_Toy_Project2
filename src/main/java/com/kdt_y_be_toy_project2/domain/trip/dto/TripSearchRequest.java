package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.kdt_y_be_toy_project2.global.util.DateTimeUtil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record TripSearchRequest(
	@NotBlank(message = "여행 이름은 필수 항목입니다.")
	String name,

	@Pattern(regexp = "(국내|국외)?", message = "국내외여부를 입력해야 합니다. (예: 국내)")
	String tripType,

	@Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)")
	String startDate,

	@Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)")
	String endDate
) {
}
