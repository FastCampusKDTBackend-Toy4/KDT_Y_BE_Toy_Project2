package com.kdt_y_be_toy_project2.domain.trip.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.member.exception.InvalidAuthException;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.dto.FindTripResponse;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripRequest;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripResponse;
import com.kdt_y_be_toy_project2.domain.trip.exception.TripNotFoundException;
import com.kdt_y_be_toy_project2.domain.trip.service.TripService;
import com.kdt_y_be_toy_project2.global.config.CustomHttpHeaders;
import com.kdt_y_be_toy_project2.global.factory.MemberTestFactory;
import com.kdt_y_be_toy_project2.global.factory.TripTestFactory;
import com.kdt_y_be_toy_project2.global.resolver.LoginInfo;
import com.kdt_y_be_toy_project2.global.util.DateTimeUtil;

@WebMvcTest(TripController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TripControllerTest {

	private final static String BASE_URL = "/v1/trips";
	private final static String FAKE_JWT_ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyLWtleSI6InRlc3RAdGVzdC5jb20iLCJpc3MiOiJLRFQtNSIsImlhdCI6MTcwMDA0NjgzOSwiZXhwIjoxNzAxMTQ3MTk5fQ.21Z9OIwgKxeTSE2jYCwqxuizJyVC4aUaYE7JeTlmCA0SEsRIDDa7C8qkpOhZb29OYbV7WDiWIIGy8RxkkFvUNw";
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private TripService tripService;

	private HttpHeaders createTestAuthHeader(String email) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(CustomHttpHeaders.ACCESS_TOKEN, FAKE_JWT_ACCESS_TOKEN);
		return headers;
	}

	@DisplayName("모든 여행 정보를 조회할 때")
	@WithMockUser
	@Nested
	class GetAllTripsTest {
		@DisplayName("모든 여행 정보 리스트를 조회할 수 있다.")
		@Test
		void getAllTripsSuccessTest() throws Exception {
			// given
			Member mockMember = MemberTestFactory.createTestMemberWithRandomPassword();
			List<Trip> mockTripList = TripTestFactory.createTestTripList(5, mockMember);

			List<TripResponse> tripResponseList = mockTripList.stream()
				.map(TripResponse::from).toList();
			given(tripService.getAllTrips()).willReturn(tripResponseList);

			// when
			ResultActions getAllTripsAction = mockMvc.perform(get(BASE_URL));

			// then
			getAllTripsAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(content().json(objectMapper.writeValueAsString(tripResponseList)));

		}

		@DisplayName("여행 정보 리스트가 없으면 예외를 발생한다.")
		@Test
		void getAllTripsFailTestWhenResponseEmptyList() throws Exception {
			// given
			given(tripService.getAllTrips()).willThrow(new TripNotFoundException());

			// when
			ResultActions getAllTripsAction = mockMvc.perform(get(BASE_URL));

			// then
			getAllTripsAction
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("여행 정보를 찾을 수 없습니다."));
		}
	}

	@DisplayName("하나의 여행 정보를 조회할 때")
	@WithMockUser
	@Nested
	class GetTripTest {
		@DisplayName("하나의 여행 정보를 조회할 수 있다.")
		@Test
		void getTripByIdSuccessTest() throws Exception {
			// given
			long tripId = 1L;
			Member mockMember = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip mockTrip = TripTestFactory.createTestTripWithID(mockMember, tripId);

			FindTripResponse tripResponse = FindTripResponse.from(mockTrip, Collections.emptyList());
			given(tripService.getTripById(anyLong())).willReturn(tripResponse);

			// when
			ResultActions getTripAction = mockMvc.perform(get(BASE_URL + "/" + tripId));

			// then
			getTripAction
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(tripResponse)));
		}

		@DisplayName("존재하지 않는 ID의 여행 정보를 가져올 수 없다.")
		@Test
		void getTripByIdFailTestWhenRequestInvalidId() throws Exception {
			// give
			long tripId = 12345L;
			given(tripService.getTripById(tripId)).willThrow(new TripNotFoundException());

			// when
			ResultActions getTripAction = mockMvc.perform(get(BASE_URL + "/" + tripId));

			// then
			getTripAction
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message").value("여행 정보를 찾을 수 없습니다."));
		}
	}

	@DisplayName("여행 정보를 등록할 때")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class CreateTripTest {

		@BeforeEach
		public void beforeEach() {
			Authentication authentication = new TestingAuthenticationToken("test@test.com", null);
			authentication.setAuthenticated(true);
			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(authentication);
		}

		@DisplayName("하나의 여행을 등록할 수 있다.")
		@Test
		void createTripSuccessTest() throws Exception {
			// given
			long tripId = 1L;
			Member mockMember = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip mockTrip = TripTestFactory.createTestTripWithID(mockMember, tripId);

			String tripTypeStr = mockTrip.getTripType().getValue();
			String startDateTimeStr = DateTimeUtil.toString(mockTrip.getTripSchedule().getStartDate());
			String endDateTimeStr = DateTimeUtil.toString(mockTrip.getTripSchedule().getEndDate());

			TripRequest request = TripRequest.builder()
				.name(mockTrip.getName())
				.tripType(tripTypeStr)
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();
			TripResponse expectedTripResponse = TripResponse.from(mockTrip);

			given(tripService.createTrip(any(TripRequest.class), any(LoginInfo.class)))
				.willReturn(expectedTripResponse);

			// when
			ResultActions createTripAction = mockMvc.perform(post(BASE_URL)
				.with(csrf())
				.headers(createTestAuthHeader(mockMember.getEmail()))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			createTripAction
				.andExpect(status().isCreated())
				.andExpect(content().json(objectMapper.writeValueAsString(expectedTripResponse)));
		}

		@DisplayName("여행 이름을 입력하지 않으면 등록할 수 없다.")
		@Test
		void createTripFailTestWhenRequestNullTripName() throws Exception {
			// given
			long tripId = 1L;
			Member mockMember = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip mockTrip = TripTestFactory.createTestTripWithID(mockMember, tripId);

			String tripTypeStr = mockTrip.getTripType().getValue();
			String startDateTimeStr = DateTimeUtil.toString(mockTrip.getTripSchedule().getStartDate());
			String endDateTimeStr = DateTimeUtil.toString(mockTrip.getTripSchedule().getEndDate());

			TripRequest request = TripRequest.builder()
				.tripType(tripTypeStr)
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions createTripAction = mockMvc.perform(post(BASE_URL)
				.with(csrf())
				.headers(createTestAuthHeader(mockMember.getEmail()))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			createTripAction
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("여행 이름을 입력해야 합니다."));
		}
	}

	@DisplayName("여행 정보를 수정할 때")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class EditTripTest {
		@BeforeEach
		public void beforeEach() {
			Authentication authentication = new TestingAuthenticationToken("test@test.com", null);
			authentication.setAuthenticated(true);
			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(authentication);
		}

		@DisplayName("하나의 여행을 수정할 수 있다.")
		@Test
		void editTripSuccessTest() throws Exception {
			// given
			long tripId = 1L;
			Member mockMember = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip mockTrip = TripTestFactory.createTestTripWithID(mockMember, tripId);

			String tripTypeStr = mockTrip.getTripType().getValue();
			String startDateTimeStr = DateTimeUtil.toString(mockTrip.getTripSchedule().getStartDate());
			String endDateTimeStr = DateTimeUtil.toString(mockTrip.getTripSchedule().getEndDate());

			TripRequest request = TripRequest.builder()
				.name(mockTrip.getName())
				.tripType(tripTypeStr)
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();
			TripResponse expectedTripResponse = TripResponse.from(mockTrip);

			given(tripService.editTrip(anyLong(), any(TripRequest.class), any(LoginInfo.class)))
				.willReturn(expectedTripResponse);

			// when
			ResultActions createTripAction = mockMvc.perform(put(BASE_URL + "/" + tripId)
				.with(csrf())
				.headers(createTestAuthHeader(mockMember.getEmail()))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			createTripAction
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(expectedTripResponse)));
		}

		@DisplayName("다른 멤버가 여행을 수정할 수 없다.")
		@Test
		void editTripFailTestWhenRequestAnotherMember() throws Exception {
			// given
			long tripId = 1L;
			Member mockMember = MemberTestFactory.createTestMemberWithRandomPassword();
			Member anotherMockMember = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip mockTrip = TripTestFactory.createTestTripWithID(mockMember, tripId);

			String tripTypeStr = mockTrip.getTripType().getValue();
			String startDateTimeStr = DateTimeUtil.toString(mockTrip.getTripSchedule().getStartDate());
			String endDateTimeStr = DateTimeUtil.toString(mockTrip.getTripSchedule().getEndDate());

			TripRequest request = TripRequest.builder()
				.name(anotherMockMember.getName())
				.tripType(tripTypeStr)
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			given(tripService.editTrip(anyLong(), any(TripRequest.class), any(LoginInfo.class)))
				.willThrow(new InvalidAuthException());

			// when
			ResultActions editTripAction = mockMvc.perform(put(BASE_URL + "/" + tripId)
				.with(csrf())
				.headers(createTestAuthHeader(anotherMockMember.getEmail()))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			editTripAction
				.andExpect(status().isUnauthorized());
		}
	}
}
