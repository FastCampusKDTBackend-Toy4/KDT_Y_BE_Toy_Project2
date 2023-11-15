package com.kdt_y_be_toy_project2.global.factory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.AccommodationInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.StayInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.TransportationType;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;

public class ItineraryFactory {

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

	public static List<Itinerary> createTestItineraryList(int size) {
		List<Itinerary> ItineraryList = new ArrayList<>();
		while (size-- > 0) {
			ItineraryList.add(createTestItinerary());
		}
		return ItineraryList;
	}
}
