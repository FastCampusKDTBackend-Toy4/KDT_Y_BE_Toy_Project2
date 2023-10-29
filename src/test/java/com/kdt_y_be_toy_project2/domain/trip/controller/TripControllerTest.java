package com.kdt_y_be_toy_project2.domain.trip.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.api.Assertions;
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
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripRequest;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.factory.TripTestFactory;
import com.kdt_y_be_toy_project2.global.util.DateTimeUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TripControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TripRepository tripRepository;

	private void assertTripResponse(Trip expectedTrip, ResultActions resultActions) throws Exception {
		// then
		resultActions
			.andExpect(jsonPath("$.tripName").value(expectedTrip.getName()))
			.andExpect(jsonPath("$.tripType").value(expectedTrip.getTripType().getValue()))
			.andExpect(jsonPath("$.startDate")
				.value(
					DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate())
				))
			.andExpect(jsonPath("$.endDate")
				.value(
					DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate())
				));
	}

	@DisplayName("여행 정보를 조회할 때")
	@Nested
	class GetTripsTest {
		@DisplayName("모든 여행 정보를 가져올 수 있다.")
		@Test
		void shouldSuccessToGetAllTrips() throws Exception {
			// given
			tripRepository.saveAll(TripTestFactory.createTestTripList(5));

			// when
			ResultActions getAllTripsAction = mockMvc.perform(get("/v1/trips"));

			// then
			getAllTripsAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
		}

		@DisplayName("하나의 여행 정보를 가져올 수 있다.")
		@Test
		void shouldSuccessToGetTripById() throws Exception {
			// given
			Trip expectedTrip = tripRepository.save(TripTestFactory.createTestTrip());
			long tripId = expectedTrip.getId();

			// when
			ResultActions getTripAction = mockMvc.perform(get("/v1/trips/" + tripId));

			// then
			getTripAction.andExpect(status().isOk());
			assertTripResponse(expectedTrip, getTripAction);
		}

		@DisplayName("존재하지 않는 ID의 여행 정보를 가져올 수 없다.")
		@Test
		void shouldFailToGetTripByIdWithInvalidId() throws Exception {
			// give
			long tripId = 12345L;

			// when
			ResultActions getTripAction = mockMvc.perform(get("/v1/trips/" + tripId));

			// then
			getTripAction
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message").value("여행 정보를 찾을 수 없습니다."));
		}
	}

	@DisplayName("여행 정보를 등록할 때")
	@Nested
	class CreateTripsTest {

		@DisplayName("하나의 여행을 등록할 수 있다.")
		@Test
		void shouldSuccessToCreateTrip() throws Exception {
			// given
			Trip expectedTrip = TripTestFactory.createTestTrip();
			String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
			String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());
			TripRequest request = TripRequest.builder()
				.name(expectedTrip.getName())
				.tripType(expectedTrip.getTripType().getValue())
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions createTripAction = mockMvc.perform(post("/v1/trips")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			createTripAction.andExpect(status().isCreated());
			assertTripResponse(expectedTrip, createTripAction);
		}

		@DisplayName("여행 이름을 입력하지 않으면 등록할 수 없다.")
		@Test
		void shouldFailToCreateTripWithEmptyName() throws Exception {
			// given
			Trip expectedTrip = TripTestFactory.createTestTrip();
			String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
			String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());
			TripRequest request = TripRequest.builder()
				.tripType(expectedTrip.getTripType().getValue())
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions createTripAction = mockMvc.perform(post("/v1/trips")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			createTripAction
				.andExpect(status().isBadRequest())
				.andExpect(res -> Assertions.assertThat(res.getResolvedException())
					.isInstanceOf(MethodArgumentNotValidException.class));
		}

		@DisplayName("여행이 끝나는 시간이 시작 시간보다 작으면 여행을 등록할 수 없다.")
		@Test
		void shouldFailToCreateTripIfEndTimeIsBeforeStartTime() throws Exception {
			// given
			Trip expectedTrip = TripTestFactory.createTestTrip();
			String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());
			String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
			TripRequest request = TripRequest.builder()
				.name(expectedTrip.getName())
				.tripType(expectedTrip.getTripType().getValue())
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions createTripAction = mockMvc.perform(post("/v1/trips")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			createTripAction
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("message").value("날짜 범위가 잘못 선택되었습니다."));
		}

		@DisplayName("잘못된 여행 타입을 입력하면 등록할 수 없다.")
		@Test
		void shouldFailToCreateTripWithInvalidTripType() throws Exception {
			// given
			Trip expectedTrip = TripTestFactory.createTestTrip();
			String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
			String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());
			TripRequest request = TripRequest.builder()
				.tripType("잘못된여행타입")
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions createTripAction = mockMvc.perform(post("/v1/trips")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			createTripAction
				.andExpect(status().isBadRequest())
				.andExpect(res -> Assertions.assertThat(res.getResolvedException())
					.isInstanceOf(MethodArgumentNotValidException.class));
		}
	}

	@DisplayName("여행 정보를 수정할 때")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class EditTripsTest {

		private Trip savedTrip;

		@BeforeAll
		void beforeAll() {
			// given
			savedTrip = tripRepository.save(TripTestFactory.createTestTrip());
		}

		@DisplayName("하나의 여행을 수정할 수 있다.")
		@Test
		void shouldSuccessToEditTrip() throws Exception {
			// given
			long savedTripId = savedTrip.getId();
			Trip expectedTrip = TripTestFactory.createTestTrip();
			String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
			String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());
			TripRequest request = TripRequest.builder()
				.name(expectedTrip.getName())
				.tripType(expectedTrip.getTripType().getValue())
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions editTripAction = mockMvc.perform(put("/v1/trips/" + savedTripId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			editTripAction.andExpect(status().isOk())
				.andExpect(jsonPath("$.tripId").value(savedTripId));
			assertTripResponse(expectedTrip, editTripAction);
		}

		@DisplayName("여행이 끝나는 시간이 시작 시간보다 작으면 여행을 수정할 수 없다.")
		@Test
		void shouldFailToCreateTripIfEndTimeIsBeforeStartTime() throws Exception {
			// given
			long savedTripId = savedTrip.getId();
			Trip expectedTrip = TripTestFactory.createTestTrip();
			String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());
			String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
			TripRequest request = TripRequest.builder()
				.name(expectedTrip.getName())
				.tripType(expectedTrip.getTripType().getValue())
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions editTripAction = mockMvc.perform(put("/v1/trips/" + savedTripId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			editTripAction
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("message").value("날짜 범위가 잘못 선택되었습니다."));
		}
	}
}