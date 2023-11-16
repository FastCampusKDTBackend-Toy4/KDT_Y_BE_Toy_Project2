package com.kdt_y_be_toy_project2.global.factory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.AccommodationInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.StayInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.TransportationType;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.AccommodationInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.MoveInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.StayInfoRequest;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;

public class ItineraryTestFactory {

	public static Itinerary createTestItinerary() {
		return Itinerary.builder()
			.accommodationInfo(AccommodationInfo.builder()
				.accommodationPlaceInfo(PlaceInfo.builder().name("test").build())
				.accommodationSchedule(DateTimeScheduleInfoTestFactory.createRandom())
				.build())
			.moveInfo(MoveInfo.builder()
				.destPlaceInfo(PlaceInfo.builder().name("test").build())
				.sourcePlaceInfo(PlaceInfo.builder().name("test").build())
				.moveSchedule(DateTimeScheduleInfoTestFactory.createRandom(LocalDateTime.now()))
				.transportationType(TransportationType.WALK)
				.build())
			.stayInfo(StayInfo.builder()
				.stayPlaceInfo(PlaceInfo.builder().name("test").build())
				.staySchedule(DateTimeScheduleInfoTestFactory.createRandom())
				.build())
			.trip(TripTestFactory.createTestTrip(MemberTestFactory.createTestMemberWithRandomPassword()))
			.build();
	}

	public static Itinerary createTestItinerary(Trip trip) {
		return Itinerary.builder()
			.accommodationInfo(AccommodationInfo.builder()
				.accommodationPlaceInfo(PlaceInfo.builder().name("test").build())
				.accommodationSchedule(DateTimeScheduleInfoTestFactory.createRandom())
				.build())
			.moveInfo(MoveInfo.builder()
				.destPlaceInfo(PlaceInfo.builder().name("test").build())
				.sourcePlaceInfo(PlaceInfo.builder().name("test").build())
				.moveSchedule(DateTimeScheduleInfoTestFactory.createRandom(LocalDateTime.now()))
				.transportationType(TransportationType.WALK)
				.build())
			.stayInfo(StayInfo.builder()
				.stayPlaceInfo(PlaceInfo.builder().name("test").build())
				.staySchedule(DateTimeScheduleInfoTestFactory.createRandom())
				.build())
			.trip(trip)
			.build();
	}

	public static ItineraryRequest buildItineraryRequest(DateTimeScheduleInfo Schedule) {
		return ItineraryRequest.builder()
			.accommodationInfoRequest(AccommodationInfoRequest.builder()
				.startDateTime(LocalDateTimeUtil.toString(Schedule.getStartDateTime()))
				.endDateTime(LocalDateTimeUtil.toString(Schedule.getEndDateTime()))
				.accommodationPlaceName("test").build())
			.stayInfoRequest(StayInfoRequest.builder()
				.startDateTime(LocalDateTimeUtil.toString(Schedule.getStartDateTime()))
				.endDateTime(LocalDateTimeUtil.toString(Schedule.getEndDateTime()))
				.stayPlaceName("test").build())
			.moveInfoRequest(MoveInfoRequest.builder()
				.startDateTime(LocalDateTimeUtil.toString(Schedule.getStartDateTime()))
				.endDateTime(LocalDateTimeUtil.toString(Schedule.getEndDateTime()))
				.sourcePlaceName("test")
				.destPlaceName("test")
				.transportationType(TransportationType.WALK.getValue()).build())
			.build();
	}

	public static List<Itinerary> createTestItineraryList(int size) {
		List<Itinerary> ItineraryList = new ArrayList<>();
		while (size-- > 0) {
			ItineraryList.add(createTestItinerary());
		}
		return ItineraryList;
	}
}
