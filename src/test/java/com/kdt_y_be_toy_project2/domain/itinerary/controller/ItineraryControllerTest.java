package com.kdt_y_be_toy_project2.domain.itinerary.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.TransportationType;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.AccommodationInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.MoveInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.StayInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.InvalidItineraryDurationException;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.ItineraryNotFoundException;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.TripNotFoundException;
import com.kdt_y_be_toy_project2.domain.itinerary.repository.ItineraryRepository;
import com.kdt_y_be_toy_project2.domain.itinerary.service.ItineraryService;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.factory.ItineraryFactory;
import com.kdt_y_be_toy_project2.global.factory.TripTestFactory;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ItineraryControllerTest {

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

	private ItineraryRequest buildItineraryRequest(DateTimeScheduleInfo Schedule) {
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

	@DisplayName("여정 정보를 조회할 때")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class GetItinerariesTest {

		private long tripId;
		private Itinerary itinerary;

		@BeforeAll
		void beforeAll() {
			// given
			Trip trip = tripRepository.save(TripTestFactory.createTestTrip());
			itinerary = itineraryRepository.save(ItineraryFactory.createTestItinerary(trip));
			tripId = trip.getId();
		}

		@DisplayName("모든 여정 정보를 가져올 수 있다.")
		@Test
		void shouldSuccessToGetAllItineraries() throws Exception {
			// when

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

			// when
			ResultActions getItineraryAction = mockMvc.perform(
				get("/v1/trips/" + tripId + "/itineraries/" + itinerary.getId()));

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
			trip = tripRepository.save(TripTestFactory.createTestTrip());
			long tripId = trip.getId();

			ItineraryRequest request = buildItineraryRequest(DateTimeScheduleInfo.builder()
				.startDateTime(LocalDateTime.from(trip.getTripSchedule().getStartDate().atStartOfDay()))
				.endDateTime(LocalDateTime.from(trip.getTripSchedule().getEndDate().atStartOfDay())).build());

			Itinerary expectedItinerary = ItineraryRequest.toEntity(request, trip);

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
			trip.getItineraries().add(savedItinerary);

			long tripId = trip.getId();
			ItineraryRequest request = buildItineraryRequest(DateTimeScheduleInfo.builder()
				.startDateTime(LocalDateTime.from(trip.getTripSchedule().getStartDate().atStartOfDay()))
				.endDateTime(LocalDateTime.from(trip.getTripSchedule().getEndDate().atStartOfDay())).build());

			long savedItineraryId = savedItinerary.getId();

			Itinerary expectedItinerary = ItineraryRequest.toEntity(request, trip);

			// when
			ResultActions editItineraryAction = mockMvc.perform(
				put("/v1/trips/" + tripId + "/itineraries/" + savedItineraryId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)));

			// then
			editItineraryAction.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(savedItineraryId));
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
			Trip trip = tripRepository.save(TripTestFactory.createTestTrip());

			// when
			ResultActions getAllItinerariesAction = mockMvc.perform(get("/v1/trips/" + trip.getId() + "/itineraries"));

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
			Trip trip = tripRepository.save(TripTestFactory.createTestTrip());
			Itinerary itinerary = itineraryRepository.save(ItineraryFactory.createTestItinerary(trip));

			// when
			ResultActions getAllItinerariesAction = mockMvc.perform(
				get("/v1/trips/" + trip.getId() + 1 + "/itineraries/" + itinerary.getId()));

			// then
			getAllItinerariesAction
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

			ItineraryRequest itineraryRequest = buildItineraryRequest(missSchedule);

			// then
			assertThrows(InvalidItineraryDurationException.class, () ->
				itineraryService.createItinerary(trip.getId(), itineraryRequest));
		}

	}
}