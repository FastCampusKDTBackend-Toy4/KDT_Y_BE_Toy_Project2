package com.kdt_y_be_toy_project2.domain.itinerary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.InvalidItineraryDurationException;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.ItineraryNotFoundException;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.TripNotFoundException;
import com.kdt_y_be_toy_project2.domain.itinerary.repository.ItineraryRepository;
import com.kdt_y_be_toy_project2.domain.itinerary.service.ItineraryService;
import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.member.repository.MemberRepository;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.config.CustomHttpHeaders;
import com.kdt_y_be_toy_project2.global.factory.ItineraryTestFactory;
import com.kdt_y_be_toy_project2.global.factory.TripTestFactory;
import com.kdt_y_be_toy_project2.global.jwt.JwtPayload;
import com.kdt_y_be_toy_project2.global.jwt.JwtProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ItineraryIntegrationTest {

    //jwt

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    private String accessToken;

    private String refreshToken;

    private void assertItineraryResponse(Itinerary expectedItinerary, ResultActions resultActions) throws Exception {
        // then
        ItineraryResponse expecteditineraryResponse = ItineraryResponse.from(expectedItinerary);

        resultActions.andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andExpect(jsonPath("$.stayInfoResponse").value(
                        objectMapper.convertValue(expecteditineraryResponse.stayInfoResponse(), LinkedHashMap.class)))
                .andExpect(jsonPath("$.moveInfoResponse").value(
                        objectMapper.convertValue(expecteditineraryResponse.moveInfoResponse(), LinkedHashMap.class)))
                .andExpect(jsonPath("$.accommodationInfoResponse").value(
                        objectMapper.convertValue(expecteditineraryResponse.accommodationInfoResponse(), LinkedHashMap.class)));
    }


    void issueJWTToken() throws Exception {
        Member testMember = Member.builder().email("test@naver.com").password(passwordEncoder.encode("1234")).name("test").build();
        Member savedMember = memberRepository.save(testMember);

        JwtPayload jwtPayload = new JwtPayload(savedMember.getEmail(), new Date());
        accessToken = jwtProvider.createAccessToken(jwtPayload);
        refreshToken = jwtProvider.createRefreshToken(jwtPayload);

    }



    @DisplayName("여정 정보를 조회할 때")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class GetItinerariesTest {

        private long tripId;
        private Itinerary itinerary;

        @BeforeAll
        void beforeAll() throws Exception {
            // given
            issueJWTToken();
            Trip trip = tripRepository.save(TripTestFactory.createTestTrip());
            itinerary = itineraryRepository.save(ItineraryTestFactory.createTestItinerary(trip));
            tripId = trip.getId();
        }

        @DisplayName("모든 여정 정보를 가져올 수 있다.")
        @Test
        void shouldSuccessToGetAllItineraries() throws Exception {
            // when
            ResultActions getAllItinerariesAction = mockMvc.perform(get("/v1/trips/" + tripId + "/itineraries")
                    .header(CustomHttpHeaders.ACCESS_TOKEN,accessToken)
                    .header(CustomHttpHeaders.REFRESH_TOKEN,refreshToken));

            // then
            getAllItinerariesAction
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @DisplayName("하나의 여정 정보를 가져올 수 있다.")
        @Test
        void shouldSuccessToGetItineraryById() throws Exception {
            // given

            // when
            ResultActions getItineraryAction = mockMvc.perform(
                    get("/v1/trips/" + tripId + "/itineraries/" + itinerary.getId())
                    .header(CustomHttpHeaders.ACCESS_TOKEN,accessToken)
                    .header(CustomHttpHeaders.REFRESH_TOKEN,refreshToken));

            // then
            assertItineraryResponse(itinerary, getItineraryAction);
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
            issueJWTToken();
            trip = tripRepository.save(TripTestFactory.createTestTrip());

            ItineraryRequest request = ItineraryTestFactory.buildItineraryRequest(DateTimeScheduleInfo.builder()
                    .startDateTime(LocalDateTime.from(trip.getTripSchedule().getStartDate().atStartOfDay()))
                    .endDateTime(LocalDateTime.from(trip.getTripSchedule().getEndDate().atStartOfDay())).build());

            Itinerary expectedItinerary = ItineraryRequest.toEntity(request, trip);

            // when
            ResultActions createTripAction = mockMvc.perform(post("/v1/trips/" + trip.getId() + "/itineraries")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header(CustomHttpHeaders.ACCESS_TOKEN,accessToken)
                    .header(CustomHttpHeaders.REFRESH_TOKEN,refreshToken));

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
            issueJWTToken();
            trip = tripRepository.save(TripTestFactory.createTestTrip());
            Itinerary savedItinerary = itineraryRepository.save(ItineraryTestFactory.createTestItinerary(trip));
            trip.getItineraries().add(savedItinerary);

            long tripId = trip.getId();
            ItineraryRequest request = ItineraryTestFactory.buildItineraryRequest(DateTimeScheduleInfo.builder()
                    .startDateTime(LocalDateTime.from(trip.getTripSchedule().getStartDate().plusDays(1).atStartOfDay()))
                    .endDateTime(LocalDateTime.from(trip.getTripSchedule().getEndDate().atStartOfDay())).build());

            long savedItineraryId = savedItinerary.getId();

            Itinerary expectedItinerary = ItineraryRequest.toEntity(request, trip);

            // when
            ResultActions editItineraryAction = mockMvc.perform(
                    put("/v1/trips/" + tripId + "/itineraries/" + savedItineraryId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .header(CustomHttpHeaders.ACCESS_TOKEN,accessToken)
                            .header(CustomHttpHeaders.REFRESH_TOKEN,refreshToken));

            // then
            editItineraryAction
                    .andDo(MockMvcResultHandlers.print());

            assertItineraryResponse(expectedItinerary, editItineraryAction);
        }
    }

    @DisplayName("예외 케이스들 집합")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class FailCase {

        @DisplayName("여정 조회 시, 여정이 존재하지 않으면 에러(ItineraryNotFound)")
        @Test
        void NotExistItinerary() throws Exception {
            // given
            issueJWTToken();
            Trip trip = tripRepository.save(TripTestFactory.createTestTrip());

            // when
            ResultActions getAllItinerariesAction = mockMvc.perform(get("/v1/trips/" + trip.getId() + "/itineraries")
                    .header(CustomHttpHeaders.ACCESS_TOKEN,accessToken)
                    .header(CustomHttpHeaders.REFRESH_TOKEN,refreshToken));

            // then
            getAllItinerariesAction
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ItineraryNotFoundException));
        }

        //여정을 조회할 때, 존재하지 않는 여행 파일을 참조할 때 (TripNotFound)
        @DisplayName("여정을 조회할 때, 여행이 존재하지 않으면 에러 (TripNotFound)")
        @Test
        void NotExistTrip() throws Exception {
            //given
            issueJWTToken();
            Trip trip = tripRepository.save(TripTestFactory.createTestTrip());
            Itinerary itinerary = itineraryRepository.save(ItineraryTestFactory.createTestItinerary(trip));

            // when
            ResultActions getAllItinerariesAction = mockMvc.perform(
                    get("/v1/trips/" + trip.getId() + 1 + "/itineraries/" + itinerary.getId())
                            .header(CustomHttpHeaders.ACCESS_TOKEN,accessToken)
                            .header(CustomHttpHeaders.REFRESH_TOKEN,refreshToken));

            // then
            getAllItinerariesAction
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is4xxClientError())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TripNotFoundException));

        }

        @DisplayName("여정 기간이 여행 기간에 포함되지 않는 경우 에러를 발생한다.")
        @Test
        void shouldNotContainsItineraryDuration() {
            //given
            Trip trip = tripRepository.save(TripTestFactory.createTestTrip());

            LocalDate tripStart = trip.getTripSchedule().getStartDate();
            LocalDate tripEnd = trip.getTripSchedule().getEndDate();

            DateTimeScheduleInfo missSchedule = DateTimeScheduleInfo.builder()
                    .startDateTime(LocalDateTime.from(tripStart.minusDays(10).atStartOfDay()))
                    .endDateTime(LocalDateTime.from(tripEnd.plusDays(10).atStartOfDay())).build();

            ItineraryRequest itineraryRequest = ItineraryTestFactory.buildItineraryRequest(missSchedule);

            // then
            assertThrows(InvalidItineraryDurationException.class, () ->
                    itineraryService.createItinerary(trip.getId(), itineraryRequest));
        }

    }
}