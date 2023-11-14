package com.kdt_y_be_toy_project2.domain.trip.dto;

import java.time.LocalDate;

import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;
import com.kdt_y_be_toy_project2.global.util.DateTimeUtil;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TripSearchRequest(
	@Size(max = 255, message = "여행 이름은 255자 이하로 검색이 가능합니다.")
	String name,

	@Pattern(regexp = "(국내|국외)?", message = "국내외여부를 입력해야 합니다. (예: 국내)")
	String tripType,

	@Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)")
	String startDate,

	@Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)")
	String endDate
) {

	public TripType getTripType() {
		return this.tripType != null ? TripType.getByValue(this.tripType) : null;
	}

	public LocalDate getStartDate() {
		return this.startDate != null ? DateTimeUtil.toLocalDateTime(this.startDate) : null;
	}

	public LocalDate getEndDate() {
		return this.endDate != null ? DateTimeUtil.toLocalDateTime(this.endDate) : null;
	}

	public boolean isAllNull() {
		return this.name == null && this.tripType == null && this.startDate == null && this.endDate == null;
	}
}
