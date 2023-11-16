package com.kdt_y_be_toy_project2.domain.itinerary.service;

import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.repository.ItineraryRepository;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.global.factory.ItineraryTestFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@ExtendWith(MockitoExtension.class)
class ItineraryServiceTest {

    @Mock
    private ItineraryRepository itineraryRepository;

    @InjectMocks
    private ItineraryService itineraryService;

    private ItineraryRequest itineraryRequest;


    @Test
    void checkInvalidDate() {
        //given
        itineraryRequest = ItineraryTestFactory
                .buildItineraryRequest(DateTimeScheduleInfo.builder()
                        .startDateTime(LocalDateTime.now().minusDays(1)).endDateTime(LocalDateTime.now()).build());
        //when & then
        assertDoesNotThrow(() -> itineraryService.checkInvalidDate(itineraryRequest));
    }
}