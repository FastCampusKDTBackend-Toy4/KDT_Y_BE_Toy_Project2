package com.kdt_y_be_toy_project2.domain.itinerary.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.InvalidItineraryDurationException;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.ItineraryNotFoundException;
import com.kdt_y_be_toy_project2.domain.itinerary.repository.ItineraryRepository;
import com.kdt_y_be_toy_project2.domain.itinerary.service.ItineraryService;
import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.member.exception.InvalidAuthException;
import com.kdt_y_be_toy_project2.domain.member.repository.MemberRepository;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.exception.TripNotFoundException;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.config.CustomHttpHeaders;
import com.kdt_y_be_toy_project2.global.factory.ItineraryTestFactory;
import com.kdt_y_be_toy_project2.global.factory.MemberTestFactory;
import com.kdt_y_be_toy_project2.global.factory.TripTestFactory;
import com.kdt_y_be_toy_project2.global.jwt.JwtPayload;
import com.kdt_y_be_toy_project2.global.jwt.service.JwtService;
import com.kdt_y_be_toy_project2.global.resolver.LoginInfo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ItineraryIntegrationTest {

	@Autowired
	private JwtService jwtService;

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

	@Autowired
	private MemberRepository memberRepository;

	private String accessToken;

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

	void issueJWTToken(Member testMember) {
		Member savedMember = memberRepository.save(testMember);

		JwtPayload jwtPayload = new JwtPayload(savedMember.getEmail(), new Date());
		accessToken = jwtService.createTokenPair(jwtPayload).accessToken();

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
			Member member = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip trip = TripTestFactory.createTestTrip(member);
			issueJWTToken(member);
			trip = tripRepository.save(trip);
			itinerary = itineraryRepository.save(ItineraryTestFactory.createTestItinerary(trip));
			tripId = trip.getId();
		}

		@DisplayName("모든 여정 정보를 가져올 수 있다.")
		@Test
		void shouldSuccessToGetAllItineraries() throws Exception {
			// when
			ResultActions getAllItinerariesAction = mockMvc.perform(get("/v1/trips/" + tripId + "/itineraries")
				.header(CustomHttpHeaders.ACCESS_TOKEN, accessToken));

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
					.header(CustomHttpHeaders.ACCESS_TOKEN, accessToken));

			// then
			assertItineraryResponse(itinerary, getItineraryAction);
		}
	}

	@DisplayName("여정 정보를 등록할 때")
	@Nested
	class CreateItinerariesTest {

		@DisplayName("하나의 여정을 등록할 수 있다.")
		@Test
		void shouldSuccessToCreateItinerary() throws Exception {
			// given
			Member member = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip trip = TripTestFactory.createTestTrip(member);
			issueJWTToken(member);
			trip = tripRepository.save(trip);

			ItineraryRequest request = ItineraryTestFactory.buildItineraryRequest(DateTimeScheduleInfo.builder()
				.startDateTime(LocalDateTime.from(trip.getTripSchedule().getStartDate().atStartOfDay()))
				.endDateTime(LocalDateTime.from(trip.getTripSchedule().getEndDate().atStartOfDay())).build());

			Itinerary expectedItinerary = itineraryService.updateItineraryWithRoadAddresses(request, trip);

			// when
			ResultActions createTripAction = mockMvc.perform(post("/v1/trips/" + trip.getId() + "/itineraries")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header(CustomHttpHeaders.ACCESS_TOKEN, accessToken));

			// then
			assertItineraryResponse(expectedItinerary, createTripAction);
		}
	}

	@DisplayName("여정 정보를 수정할 때")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	class EditItineraryTest {

		@DisplayName("하나의 여정을 수정할 수 있다.")
		@Test
		void shouldSuccessToEditItinerary() throws Exception {
			// given
			Member member = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip trip = TripTestFactory.createTestTrip(member);
			issueJWTToken(member);
			trip = tripRepository.save(trip);

			Itinerary savedItinerary = itineraryRepository.save(ItineraryTestFactory.createTestItinerary(trip));
			trip.getItineraries().add(savedItinerary);

			long tripId = trip.getId();
			ItineraryRequest request = ItineraryTestFactory.buildItineraryRequest(DateTimeScheduleInfo.builder()
				.startDateTime(LocalDateTime.from(trip.getTripSchedule().getStartDate().plusDays(1).atStartOfDay()))
				.endDateTime(LocalDateTime.from(trip.getTripSchedule().getEndDate().atStartOfDay())).build());

			long savedItineraryId = savedItinerary.getId();

			Itinerary expectedItinerary = itineraryService.updateItineraryWithRoadAddresses(request, trip);

			// when
			ResultActions editItineraryAction = mockMvc.perform(
				put("/v1/trips/" + tripId + "/itineraries/" + savedItineraryId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
					.header(CustomHttpHeaders.ACCESS_TOKEN, accessToken));

			// then
			editItineraryAction
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk());

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
			Member member = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip trip = TripTestFactory.createTestTrip(member);
			issueJWTToken(member);
			trip = tripRepository.save(trip);

			// when
			ResultActions getAllItinerariesAction = mockMvc.perform(get("/v1/trips/" + trip.getId() + "/itineraries")
				.header(CustomHttpHeaders.ACCESS_TOKEN, accessToken));

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
			Member member = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip trip = TripTestFactory.createTestTrip(member);
			issueJWTToken(member);
			trip = tripRepository.save(trip);

			Itinerary itinerary = itineraryRepository.save(ItineraryTestFactory.createTestItinerary(trip));

			// when
			ResultActions getAllItinerariesAction = mockMvc.perform(
				get("/v1/trips/" + trip.getId() + 1 + "/itineraries/" + itinerary.getId())
					.header(CustomHttpHeaders.ACCESS_TOKEN, accessToken));

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
			Member member = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip trip = TripTestFactory.createTestTrip(member);
			issueJWTToken(member);
			final Trip savedTrip = tripRepository.save(trip);

			LocalDate tripStart = savedTrip.getTripSchedule().getStartDate();
			LocalDate tripEnd = savedTrip.getTripSchedule().getEndDate();
			LoginInfo loginInfo = new LoginInfo(savedTrip.getMember().getEmail());

			DateTimeScheduleInfo missSchedule = DateTimeScheduleInfo.builder()
				.startDateTime(LocalDateTime.from(tripStart.minusDays(10).atStartOfDay()))
				.endDateTime(LocalDateTime.from(tripEnd.plusDays(10).atStartOfDay())).build();

			ItineraryRequest itineraryRequest = ItineraryTestFactory.buildItineraryRequest(missSchedule);

			// then
			assertThrows(InvalidItineraryDurationException.class, () ->
				itineraryService.createItinerary(loginInfo, savedTrip.getId(), itineraryRequest));
		}

		@DisplayName("여정 수정 권한이 없어서 에러를 발생한다.")
		@Test
		void shouldNotAccessToUpdateItinearary() throws Exception {
			//given
			Member member = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip testTrip = TripTestFactory.createTestTrip(member);
			memberRepository.save(member);
			Trip savedtrip = tripRepository.save(testTrip);
			//여행을 저장한 멤버가 아닌, 새로운 멤버가 생성되고, JWT TOKEN을 발급받음.
			issueJWTToken(MemberTestFactory.createTestMemberWithRandomPassword());

			Itinerary savedItinerary = itineraryRepository.save(ItineraryTestFactory.createTestItinerary(savedtrip));
			savedtrip.getItineraries().add(savedItinerary);

			ItineraryRequest request = ItineraryTestFactory.buildItineraryRequest(DateTimeScheduleInfo.builder()
				.startDateTime(
					LocalDateTime.from(savedtrip.getTripSchedule().getStartDate().plusDays(1).atStartOfDay()))
				.endDateTime(LocalDateTime.from(savedtrip.getTripSchedule().getEndDate().atStartOfDay())).build());

			// when
			ResultActions editItineraryAction = mockMvc.perform(
				put("/v1/trips/" + savedtrip.getId() + "/itineraries/" + savedItinerary.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
					.header(CustomHttpHeaders.ACCESS_TOKEN, accessToken));

			// then
			editItineraryAction
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isUnauthorized())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidAuthException));
		}

	}
}