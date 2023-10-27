package com.kdt_y_be_toy_project2.global.factory;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.*;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItineraryFactory {


    public static Itinerary createTestItinerary() {
        return Itinerary.builder()
                .accommodationInfo(AccommodationInfo.builder()
                        .accommodationSchedule(DateScheduleInfoTestFactory.createRandom2())
                        .build())
                .moveInfo(MoveInfo.builder()
                        .moveSchedule(DateScheduleInfoTestFactory.createRandom2(LocalDateTime.now()))
                        .transportationType(TransportationType.WALK)
                        .build())
                .stayInfo(StayInfo.builder()
                        .staySchedule(DateScheduleInfoTestFactory.createRandom2())
                        .build())
                .trip(TripTestFactory.createTestTrip())
                .build();
    }

    public static Itinerary createTestItinerary(Trip trip) {
        return Itinerary.builder()
                .accommodationInfo(AccommodationInfo.builder()
                        .accommodationSchedule(DateScheduleInfoTestFactory.createRandom2())
                        .build())
                .moveInfo(MoveInfo.builder()
                        .moveSchedule(DateScheduleInfoTestFactory.createRandom2(LocalDateTime.now()))
                        .transportationType(TransportationType.WALK)
                        .build())
                .stayInfo(StayInfo.builder()
                        .staySchedule(DateScheduleInfoTestFactory.createRandom2())
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
