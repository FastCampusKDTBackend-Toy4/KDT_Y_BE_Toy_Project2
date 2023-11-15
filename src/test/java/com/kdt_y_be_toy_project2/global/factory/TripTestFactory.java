package com.kdt_y_be_toy_project2.global.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.type.TripType;

public class TripTestFactory {

	public static Trip createTestTrip() {
		int randomTripTypeIndex = ThreadLocalRandom.current().nextInt(TripType.values().length);
		TripType randomTripType = TripType.values()[randomTripTypeIndex];

		return Trip.builder()
			.name("여행테스트" + ThreadLocalRandom.current().nextInt(1000))
			.member(MemberTestFactory.createTestMemberWithRandomPassword())
			.tripSchedule(DateScheduleInfoTestFactory.createRandom())
			.tripType(randomTripType)
			.build();
	}

	public static List<Trip> createTestTripList(int size) {
		List<Trip> tripList = new ArrayList<>();
		while (size-- > 0) {
			tripList.add(createTestTrip());
		}
		return tripList;
	}
}
