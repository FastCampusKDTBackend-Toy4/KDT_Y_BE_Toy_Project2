package com.kdt_y_be_toy_project2.domain.itinerary.controller;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.service.ItineraryService;
import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.member.repository.MemberRepository;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.global.factory.ItineraryTestFactory;
import com.kdt_y_be_toy_project2.global.factory.MemberTestFactory;
import com.kdt_y_be_toy_project2.global.factory.TripTestFactory;
import com.kdt_y_be_toy_project2.global.resolver.LoginInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(ItineraryController.class)
@ExtendWith(MockitoExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class ItineraryControllerTest {

    @MockBean
    private ItineraryService itineraryService;

    @Autowired
    private ItineraryController itineraryController;

    private ItineraryRequest itineraryRequest;

    private Itinerary itinerary;

    private LoginInfo loginInfo;

    @BeforeEach
    void initItinerary() {
        Member member = MemberTestFactory.createTestMemberWithRandomPassword();
        itineraryRequest = ItineraryTestFactory
                .buildItineraryRequest(DateTimeScheduleInfo.builder()
                        .startDateTime(LocalDateTime.now())
                        .endDateTime(LocalDateTime.now())
                        .build());
        itinerary = ItineraryRequest.toEntity(itineraryRequest, TripTestFactory.createTestTrip(member));
        loginInfo = new LoginInfo(itinerary.getTrip().getMember().getEmail());
    }

    @Test
    void editItinerary() {
        //given
        ItineraryResponse itineraryResponse = ItineraryResponse.from(itinerary);
        when(itineraryService
                .editItinerary(loginInfo, 1L, 1L, itineraryRequest))
                .thenReturn(itineraryResponse);

        //when
        ResponseEntity<ItineraryResponse> responseResponseEntity = itineraryController.editItinerary(loginInfo,
                1L, 1L, itineraryRequest);

        //then
        assertEquals(responseResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        assertEquals(responseResponseEntity.getBody(), itineraryResponse);
    }
}