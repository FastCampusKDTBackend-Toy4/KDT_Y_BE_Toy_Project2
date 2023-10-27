package com.kdt_y_be_toy_project2.domain.itinerary.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.AccommodationInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.StayInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.*;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.InvalidItineraryDurationException;
import com.kdt_y_be_toy_project2.domain.itinerary.repository.ItineraryRepository;
import com.kdt_y_be_toy_project2.domain.model.DateScheduleInfo;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.global.factory.ItineraryFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.factory.TripTestFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ItineraryControllerTest {

    private MockMvc mockMvc;


    private ObjectMapper objectMapper;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    private void assertItineraryResponse(Itinerary expectedItinerary, ResultActions resultActions) throws Exception {
        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.stayInfo").value(
                        objectMapper.convertValue(expectedItinerary.getStayInfo(), LinkedHashMap.class)))
                .andExpect(jsonPath("$.moveInfo").value(
                        objectMapper.convertValue(expectedItinerary.getMoveInfo(), LinkedHashMap.class)))
                .andExpect(jsonPath("$.accommodationPlaceInfo").value(
                        objectMapper.convertValue(expectedItinerary.getAccommodationInfo(), LinkedHashMap.class)));

    }

    @DisplayName("여정 정보를 조회할 때")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class GetItinerariesTest {

        private List<Itinerary> savedItineraryList;

        @BeforeAll
        void beforeAll() {
            // given
            savedItineraryList = itineraryRepository.saveAll(ItineraryFactory.createTestItineraryList(5));
        }

        @DisplayName("모든 여정 정보를 가져올 수 있다.")
        @Test
        void shouldSuccessToGetAllItineraries() throws Exception {
            // when
            Long tripId = savedItineraryList.get(0).getTrip().getId();
            ResultActions getAllItinerariesAction = mockMvc.perform(get("/v1/trips/" + tripId + "/itineraries"));

            // then
            getAllItinerariesAction
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @DisplayName("하나의 여정 정보를 가져올 수 있다.")
        @Test
        void shouldSuccessToGetItineraryById() throws Exception {
            // given
            Itinerary expectedItinerary = savedItineraryList.get(0);
            long tripId = expectedItinerary.getTrip().getId();
            long itineraryId = expectedItinerary.getId();
            // when
            ResultActions getItineraryAction = mockMvc.perform(get("/v1/trips/" + tripId + "/itineraries/" + itineraryId));

            // then
            assertItineraryResponse(expectedItinerary, getItineraryAction);
        }
    }

    @DisplayName("여정 정보를 등록할 때")
    @Nested
    class CreateItinerariesTest {
        Trip trip;

        @DisplayName("하나의 여정을 등록할 수 있다.")
        @Test
        void shouldSuccessToCreateItinerary() throws Exception {
            // given
            trip = tripRepository.save(TripTestFactory.createTestTrip());
            Itinerary expectedItinerary = ItineraryFactory.createTestItinerary(trip);
            long tripId = trip.getId();

            ItineraryRequest request = ItineraryRequest.builder()
                    .stayInfoRequest(StayInfoRequest.builder()
                            .startDateTime(expectedItinerary.getStayInfo().getStaySchedule().getStartDateTime())
                            .endDateTime(expectedItinerary.getStayInfo().getStaySchedule().getEndDateTime()).build())
                    .moveInfoRequest(MoveInfoRequest.builder()
                            .startDateTime(expectedItinerary.getMoveInfo().getMoveSchedule().getStartDateTime())
                            .endDateTime(expectedItinerary.getMoveInfo().getMoveSchedule().getEndDateTime()).build())
                    .accommodationInfoRequest(AccommodationInfoRequest.builder()
                            .startDateTime(expectedItinerary.getAccommodationInfo().getAccommodationSchedule().getStartDateTime())
                            .endDateTime(expectedItinerary.getAccommodationInfo().getAccommodationSchedule().getEndDateTime()).build())
                    .build();

            // when
            ResultActions createTripAction = mockMvc.perform(post("/v1/trips/" + tripId + "/itineraries")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            assertItineraryResponse(expectedItinerary, createTripAction);
        }
    }

    @DisplayName("여정 정보를 수정할 때")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class EditItineraryTest {
        Trip trip;

        @DisplayName("하나의 여정을 수정할 수 있다.")
        @Test
        void shouldSuccessToEditItinerary() throws Exception {
            // given
            trip = tripRepository.save(TripTestFactory.createTestTrip());
            Itinerary savedItinerary = itineraryRepository.save(ItineraryFactory.createTestItinerary(trip));

            long tripId = trip.getId();
            Itinerary expectedItinerary = ItineraryFactory.createTestItinerary(trip);
            long savedItineraryId = savedItinerary.getId();

            ItineraryRequest request = ItineraryRequest.builder()
                    .stayInfoRequest(StayInfoRequest.builder()
                            .startDateTime(expectedItinerary.getStayInfo().getStaySchedule().getStartDateTime())
                            .endDateTime(expectedItinerary.getStayInfo().getStaySchedule().getEndDateTime()).build())
                    .moveInfoRequest(MoveInfoRequest.builder()
                            .startDateTime(expectedItinerary.getMoveInfo().getMoveSchedule().getStartDateTime())
                            .endDateTime(expectedItinerary.getMoveInfo().getMoveSchedule().getEndDateTime()).build())
                    .accommodationInfoRequest(AccommodationInfoRequest.builder()
                            .startDateTime(expectedItinerary.getAccommodationInfo().getAccommodationSchedule().getStartDateTime())
                            .endDateTime(expectedItinerary.getAccommodationInfo().getAccommodationSchedule().getEndDateTime()).build())
                    .build();

            // when
            ResultActions editItineraryAction = mockMvc.perform(put("/v1/trips/" + tripId + "/itineraries/" + savedItineraryId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            editItineraryAction.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(savedItineraryId));
            assertItineraryResponse(expectedItinerary, editItineraryAction);
        }
    }

    @DisplayName("여정 기간이 여행 기간에 포함되지 않는 경우 에러를 발생한다.")
    @Test
    void 여정기간이_여행기간에_포함되지_않는경우() throws Exception {
        //given
        LocalDate tripStart = LocalDate.of(2023,10,10);
        LocalDate tripEnd = LocalDate.of(2023,10,12);
        Trip trip = Trip.builder()
                        .tripSchedule(DateScheduleInfo.builder()
                                .startDate(tripStart)
                                .endDate(tripEnd)
                                .build())
                                .build();


        DateTimeScheduleInfo missSchedule = DateTimeScheduleInfo.builder()
                .startDateTime(LocalDateTime.from(tripStart.minusDays(10)))
                .endDateTime(LocalDateTime.from(tripStart.plusDays(10))).build();

        Itinerary itinerary = Itinerary.builder()
                .trip(trip)
                .stayInfo(StayInfo.builder().staySchedule(missSchedule).build())
                .moveInfo(MoveInfo.builder().moveSchedule(missSchedule).build())
                .accommodationInfo(AccommodationInfo.builder().accommodationSchedule(missSchedule).build())
                .build();
        // then
        assertThrows(InvalidItineraryDurationException.class, () ->
                itineraryRepository.save(itinerary));
    }
}