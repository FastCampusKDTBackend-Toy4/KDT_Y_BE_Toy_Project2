package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.kdt_y_be_toy_project2.domain.model.DateScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;
import com.kdt_y_be_toy_project2.global.util.DateTimeUtil;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record TripRequest(
	@Schema(description = "여행 이름", defaultValue = "테스트 여행")
	@NotBlank(message = "여행 이름을 입력해야 합니다.")
	String name,

	@Schema(description = "여행 시작 일자", defaultValue = "2023-10-25")
	@NotNull(message = "여행 시작 시간을 입력해야 합니다.")
	@Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)")
	String startDate,

	@Schema(description = "여행 종료 일자", defaultValue = "2023-10-25")
	@NotNull(message = "여행 종료 시간을 입력해야 합니다.")
	@Pattern(regexp = DateTimeUtil.LOCAL_DATE_REGEX_PATTERN, message = "잘못된 시간 형식입니다. (올바른 예시: 2023-10-25)")
	String endDate,

	@Schema(description = "국내외여부", defaultValue = "국내")
	@NotNull(message = "국내외여부를 입력해야 합니다. (예: 국내)")
	String tripType
) {
	public static Trip toEntity(final TripRequest request) {
		return Trip.builder()
			.name(request.name)
			.tripType(TripType.getByValue(request.tripType))
			.tripSchedule(
				DateScheduleInfo.builder()
					.startDate(DateTimeUtil.toLocalDateTime(request.startDate))
					.endDate(DateTimeUtil.toLocalDateTime(request.endDate))
					.build()
			).build();
	}
}
