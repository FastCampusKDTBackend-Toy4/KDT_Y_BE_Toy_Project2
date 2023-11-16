package com.kdt_y_be_toy_project2.domain.trip.dto;

import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.global.util.DateTimeUtil;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TripResponse(
	@Schema(description = "여행 ID")
	Long tripId,

	@Schema(description = "여행 이름")
	String tripName,

	@Schema(description = "여행 시작 일자")
	String startDate,

	@Schema(description = "여행 종료 일자")
	String endDate,

	@Schema(description = "여행 국내외여부")
	String tripType,

	@Schema(description = "여행 좋아요 개수")
	Long likesCount
) {
	public static TripResponse from(Trip trip) {
		return TripResponse.builder()
			.tripId(trip.getId())
			.tripName(trip.getName())
			.startDate(DateTimeUtil.toString(trip.getTripSchedule().getStartDate()))
			.endDate(DateTimeUtil.toString(trip.getTripSchedule().getEndDate()))
			.tripType(trip.getTripType().getValue())
			.likesCount(trip.getLikesCount())
			.build();
	}
}
