package com.kdt_y_be_toy_project2.global.factory;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.*;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.AccommodationInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.MoveInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.StayInfoRequest;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class ItineraryTestFactory {

    public static ItineraryRequest createTestItineraryRequest() {
        int randomTripTypeIndex = ThreadLocalRandom.current().nextInt(TransportationType.values().length);
        TransportationType randomTransportationType = TransportationType.values()[randomTripTypeIndex];

        String startDateTime = LocalDateTimeUtil.toString(randomSchedule().getStartDateTime());
        String endDateTime = LocalDateTimeUtil.toString(randomSchedule().getEndDateTime());

        return ItineraryRequest.builder()
                .moveInfoRequest(MoveInfoRequest.builder().destPlaceName(randomPlace().getName()).sourcePlaceName(randomPlace().getName()).startDateTime(startDateTime).endDateTime(endDateTime).transportationType(randomTransportationType.getValue()).build())
                .stayInfoRequest(StayInfoRequest.builder().stayPlaceName(randomPlace().getName()).startDateTime(startDateTime).endDateTime(endDateTime).build())
                .accommodationInfoRequest(AccommodationInfoRequest.builder().accommodationPlaceName(randomPlace().getName()).startDateTime(startDateTime).endDateTime(endDateTime).build())
                .build();
    }

    public static ItineraryRequest createSuccessTestItineraryRequest() {
        int randomTripTypeIndex = ThreadLocalRandom.current().nextInt(TransportationType.values().length);
        TransportationType randomTransportationType = TransportationType.values()[randomTripTypeIndex];

        String startDateTime = LocalDateTimeUtil.toString(LocalDateTime.now());
        String endDateTime = LocalDateTimeUtil.toString(LocalDateTime.now());

        return ItineraryRequest.builder()
                .moveInfoRequest(MoveInfoRequest.builder().destPlaceName(randomPlace().getName()).sourcePlaceName(randomPlace().getName()).startDateTime(startDateTime).endDateTime(endDateTime).transportationType(randomTransportationType.getValue()).build())
                .stayInfoRequest(StayInfoRequest.builder().stayPlaceName(randomPlace().getName()).startDateTime(startDateTime).endDateTime(endDateTime).build())
                .accommodationInfoRequest(AccommodationInfoRequest.builder().accommodationPlaceName(randomPlace().getName()).startDateTime(startDateTime).endDateTime(endDateTime).build())
                .build();
    }

    public static PlaceInfo randomPlace(){
        return PlaceInfo.builder().name("여행테스트" + ThreadLocalRandom.current().nextInt(1000)).build();
    }

    public static DateTimeScheduleInfo randomSchedule(){
        return DateTimeScheduleInfoTestFactory.createRandom();
    }

}
