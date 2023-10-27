package com.kdt_y_be_toy_project2.domain.trip.dto;

import java.util.List;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.global.util.DateTimeUtil;

import lombok.Builder;

@Builder
public record TripResponse(
	Long tripId,
	String tripName,
	String startDate,
	String endDate,
	String tripType,
	List<Itinerary> itineraries
) {
	public static TripResponse from(Trip trip) {
		return TripResponse.builder()
			.tripId(trip.getId())
			.tripName(trip.getName())
			.startDate(DateTimeUtil.toString(trip.getTripSchedule().getStartDate()))
			.endDate(DateTimeUtil.toString(trip.getTripSchedule().getEndDate()))
			.tripType(trip.getTripType().getValue())
			.itineraries(trip.getItineraries())
			.build();
	}
}
