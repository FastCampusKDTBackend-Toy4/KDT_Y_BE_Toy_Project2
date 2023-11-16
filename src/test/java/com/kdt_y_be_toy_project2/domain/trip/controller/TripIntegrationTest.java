package com.kdt_y_be_toy_project2.domain.trip.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.member.repository.MemberRepository;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripRequest;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.config.CustomHttpHeaders;
import com.kdt_y_be_toy_project2.global.factory.MemberTestFactory;
import com.kdt_y_be_toy_project2.global.factory.TripTestFactory;
import com.kdt_y_be_toy_project2.global.jwt.JwtPayload;
import com.kdt_y_be_toy_project2.global.jwt.service.JwtService;
import com.kdt_y_be_toy_project2.global.util.DateTimeUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TripIntegrationTest {

	private final static String BASE_URL = "/v1/trips";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TripRepository tripRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private JwtService jwtService;

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

	private HttpHeaders createTestAuthHeader(String email) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(
			CustomHttpHeaders.ACCESS_TOKEN,
			jwtService.createTokenPair(new JwtPayload(email, new Date())).accessToken()
		);
		return headers;
	}

	@DisplayName("여행 정보를 조회할 때")
	@Nested
	class GetTripsTest {
		@DisplayName("모든 여행 정보를 가져올 수 있다.")
		@Test
		void shouldSuccessToGetAllTrips() throws Exception {
			// given
			Member member = MemberTestFactory.createTestMemberWithRandomPassword();
			tripRepository.saveAll(TripTestFactory.createTestTripList(5, member));

			// when
			ResultActions getAllTripsAction = mockMvc.perform(get(BASE_URL));

			// then
			getAllTripsAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
		}

		@DisplayName("하나의 여행 정보를 가져올 수 있다.")
		@Test
		void shouldSuccessToGetTripById() throws Exception {
			// given
			Member member = MemberTestFactory.createTestMemberWithRandomPassword();
			Trip expectedTrip = tripRepository.save(TripTestFactory.createTestTrip(member));
			long tripId = expectedTrip.getId();

			// when
			ResultActions getTripAction = mockMvc.perform(get(BASE_URL + "/" + tripId));

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
			ResultActions getTripAction = mockMvc.perform(get(BASE_URL + "/" + tripId));

			// then
			getTripAction
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message").value("여행 정보를 찾을 수 없습니다."));
		}
	}

	@DisplayName("여행 정보를 등록할 때")
	@Nested
	class CreateTripsTest {

		private Member member;
		private HttpHeaders testAuthHeaders;

		@BeforeEach
		public void beforeEach() {
			member = memberRepository.save(MemberTestFactory.createTestMemberWithRandomPassword());
			testAuthHeaders = createTestAuthHeader(member.getEmail());
		}

		@DisplayName("하나의 여행을 등록할 수 있다.")
		@Test
		void shouldSuccessToCreateTrip() throws Exception {
			// given
			Trip expectedTrip = TripTestFactory.createTestTrip(member);
			String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
			String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());
			TripRequest request = TripRequest.builder()
				.name(expectedTrip.getName())
				.tripType(expectedTrip.getTripType().getValue())
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions createTripAction = mockMvc.perform(post(BASE_URL)
				.headers(testAuthHeaders)
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
			Trip expectedTrip = TripTestFactory.createTestTrip(member);
			String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
			String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());
			TripRequest request = TripRequest.builder()
				.tripType(expectedTrip.getTripType().getValue())
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions createTripAction = mockMvc.perform(post(BASE_URL)
				.headers(testAuthHeaders)
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
			Trip expectedTrip = TripTestFactory.createTestTrip(member);
			String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());
			String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
			TripRequest request = TripRequest.builder()
				.name(expectedTrip.getName())
				.tripType(expectedTrip.getTripType().getValue())
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions createTripAction = mockMvc.perform(post(BASE_URL)
				.headers(testAuthHeaders)
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
			Trip expectedTrip = TripTestFactory.createTestTrip(member);
			String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
			String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());
			TripRequest request = TripRequest.builder()
				.tripType("잘못된여행타입")
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions createTripAction = mockMvc.perform(post(BASE_URL)
				.headers(testAuthHeaders)
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
	@Nested
	class EditTripsTest {

		private Member member;
		private HttpHeaders testAuthHeaders;
		private Trip savedTrip;

		@BeforeEach
		void beforeEach() {
			// given
			member = MemberTestFactory.createTestMemberWithRandomPassword();
			testAuthHeaders = createTestAuthHeader(member.getEmail());
			savedTrip = tripRepository.save(TripTestFactory.createTestTrip(member));
		}

		@DisplayName("하나의 여행을 수정할 수 있다.")
		@Test
		void shouldSuccessToEditTrip() throws Exception {
			// given
			long savedTripId = savedTrip.getId();
			Trip expectedTrip = TripTestFactory.createTestTrip(member);
			String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
			String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());
			TripRequest request = TripRequest.builder()
				.name(expectedTrip.getName())
				.tripType(expectedTrip.getTripType().getValue())
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions editTripAction = mockMvc.perform(put(BASE_URL + "/" + savedTripId)
				.headers(testAuthHeaders)
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
			Trip expectedTrip = TripTestFactory.createTestTrip(member);
			String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());
			String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
			TripRequest request = TripRequest.builder()
				.name(expectedTrip.getName())
				.tripType(expectedTrip.getTripType().getValue())
				.startDate(startDateTimeStr)
				.endDate(endDateTimeStr)
				.build();

			// when
			ResultActions editTripAction = mockMvc.perform(put(BASE_URL + "/" + savedTripId)
				.headers(testAuthHeaders)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

			// then
			editTripAction
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("message").value("날짜 범위가 잘못 선택되었습니다."));
		}
	}

	@DisplayName("여행 정보를 검색할 때")
	@Nested
	class SearchTripTest {

		private List<Trip> savedTripList;

		/**
		 * 여행을 검색하는데 사용될 파라미터의 경우의 수를 만드는 메소드입니다.
		 * 여행의 파라미터는 총 4가지의 파라미터를 가지고 있고,
		 * 4가지의 파라미터를 boolean 값으로 내보내서 경우의 수를 만들어냅니다.
		 * 예를 들면 결과값의 10번째(1010) 값은 다음과 같습니다.
		 * ```
		 * {name=true, tripType=false, startDate=true, endDate=false}
		 * ```
		 *
		 * @return TestQueryCheck
		 */
		static Stream<TestQueryCheck> searchQueryTestCase() {
			ArrayList<TestQueryCheck> list = new ArrayList<>();
			for (int i = 1; i < (1 << 4); i++) {
				list.add(new TestQueryCheck(
					(i & 1) == 1,
					(i & (1 << 1)) > 0,
					(i & (1 << 2)) > 0,
					(i & (1 << 3)) > 0
				));
			}
			return list.stream();
		}

		@BeforeEach
		void beforeEach() {
			// given
			Member member = MemberTestFactory.createTestMemberWithRandomPassword();
			savedTripList = tripRepository.saveAll(TripTestFactory.createTestTripList(5, member));
		}

		@DisplayName("1개 이상의 항목을 포함한 쿼리로 검색할 수 있다.")
		@MethodSource("searchQueryTestCase")
		@ParameterizedTest(name = "{arguments}")
		void shouldSuccessToSearchTripWithQueries(TestQueryCheck searchQueryCheck) throws Exception {
			// given
			Trip expectedTrip = savedTripList.get(0);

			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(BASE_URL + "/" + "search");
			if (searchQueryCheck.name)
				uriBuilder.queryParam("name", expectedTrip.getName());
			if (searchQueryCheck.tripType)
				uriBuilder.queryParam("tripType", expectedTrip.getTripType().getValue());
			if (searchQueryCheck.startDate)
				uriBuilder.queryParam("startDate",
					expectedTrip.getTripSchedule().getStartDate().format(DateTimeFormatter.ISO_DATE));
			if (searchQueryCheck.endDate)
				uriBuilder.queryParam("endDate",
					expectedTrip.getTripSchedule().getEndDate().format(DateTimeFormatter.ISO_DATE));

			// when
			ResultActions searchTripsAction = mockMvc.perform(get(uriBuilder.build().toString()));

			// then
			searchTripsAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
		}

		private static class TestQueryCheck {
			boolean name;
			boolean tripType;
			boolean startDate;
			boolean endDate;

			public TestQueryCheck(boolean name, boolean tripType, boolean startDate, boolean endDate) {
				this.name = name;
				this.tripType = tripType;
				this.startDate = startDate;
				this.endDate = endDate;
			}

			@Override
			public String toString() {
				return (name ? "name," : "") +
					(tripType ? "tripType," : "") +
					(startDate ? "startDate," : "") +
					(endDate ? "endDate" : "");
			}
		}
	}

	@DisplayName("여행 정보 좋아요 관련 테스트")
	@Nested
	class TripLikesTest {

		private Trip savedTrip;

		private HttpHeaders testAuthHeaders;

		@BeforeEach
		void beforeEach() {
			// given
			Member member = MemberTestFactory.createTestMemberWithRandomPassword();
			savedTrip = tripRepository.save(TripTestFactory.createTestTrip(member));
			testAuthHeaders = createTestAuthHeader(member.getEmail());
		}

		@DisplayName("멤버는 자신이 좋아요를 눌렀던 여행 리스트를 볼 수 있다.")
		@Test
		void shouldSuccessToGetMyLikesTripList() throws Exception {
			// given

			// when
			ResultActions getLikesTripAction = mockMvc.perform(get(BASE_URL + "/my/likes")
				.headers(testAuthHeaders)
			);

			// then
			getLikesTripAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
		}

		@DisplayName("멤버는 여행에 좋아요를 누를 수 있다.")
		@Test
		void shouldSuccessToLikeTrip() throws Exception {
			// given
			long savedTripId = savedTrip.getId();

			// when
			ResultActions likesTripAction = mockMvc.perform(post(BASE_URL + "/" + savedTripId + "/likes")
				.headers(testAuthHeaders)
			);

			// then
			likesTripAction
				.andExpect(status().isOk());
		}
	}
}