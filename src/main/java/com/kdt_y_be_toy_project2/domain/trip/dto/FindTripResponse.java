package com.kdt_y_be_toy_project2.domain.trip.dto;

import java.util.List;
import java.util.Map;

import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryResponse;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.global.util.DateTimeUtil;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record FindTripResponse(
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

	@Schema(description = "여행에 포함된 여정 리스트")
	List<ItineraryResponse> itineraries,

	@Schema(description = "여행 댓글")
	CommentResponse commentResponse
) {
	public static FindTripResponse from(Trip trip, List<Map<String, Object>> lists) {
		CommentResponse commentResponse = new CommentResponse(lists);

		return FindTripResponse.builder()
			.tripId(trip.getId())
			.tripName(trip.getName())
			.startDate(DateTimeUtil.toString(trip.getTripSchedule().getStartDate()))
			.endDate(DateTimeUtil.toString(trip.getTripSchedule().getEndDate()))
			.tripType(trip.getTripType().getValue())
			.itineraries(trip.getItineraries().stream().map(ItineraryResponse::from).toList())
			.commentResponse(commentResponse)
			.build();
	}
}
