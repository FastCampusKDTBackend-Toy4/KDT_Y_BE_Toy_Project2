package com.kdt_y_be_toy_project2.global.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.type.TripType;

public class TripTestFactory {

	public static Trip createTestTrip(Member member) {
		int randomTripTypeIndex = ThreadLocalRandom.current().nextInt(TripType.values().length);
		TripType randomTripType = TripType.values()[randomTripTypeIndex];

		return Trip.builder()
			.name("여행테스트" + ThreadLocalRandom.current().nextInt(1000))
			.member(member)
			.tripSchedule(DateScheduleInfoTestFactory.createRandom())
			.tripType(randomTripType)
			.build();
	}

	public static Trip createTestTripWithID(Member member, long id) {
		int randomTripTypeIndex = ThreadLocalRandom.current().nextInt(TripType.values().length);
		TripType randomTripType = TripType.values()[randomTripTypeIndex];

		return Trip.builder()
			.name("여행테스트" + ThreadLocalRandom.current().nextInt(1000))
			.id(id)
			.member(member)
			.tripSchedule(DateScheduleInfoTestFactory.createRandom())
			.tripType(randomTripType)
			.build();
	}

	public static List<Trip> createTestTripList(int size, Member member) {
		List<Trip> tripList = new ArrayList<>();
		for (int i = 1; i <= size; i++) {
			tripList.add(createTestTripWithID(member, i));
		}
		return tripList;
	}
}
