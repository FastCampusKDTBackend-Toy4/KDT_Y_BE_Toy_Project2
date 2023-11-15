package com.kdt_y_be_toy_project2.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.TransportationType;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.AccommodationInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.MoveInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.StayInfoRequest;
import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.member.repository.MemberRepository;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripRequest;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripResponse;
import com.kdt_y_be_toy_project2.global.factory.MemberTestFactory;
import com.kdt_y_be_toy_project2.global.factory.TripTestFactory;
import com.kdt_y_be_toy_project2.global.util.DateTimeUtil;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TripAndItineraryIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MemberRepository memberRepository;

	@DisplayName("사용자는 여행 정보를 만든 후에 여정 정보를 등록할 수 있다.")
	@Test
	public void createItineraryAfterCreatingTrip() throws Exception {
		// given
		Member member = memberRepository.save(MemberTestFactory.createTestMemberWithRandomPassword());
		Trip expectedTrip = TripTestFactory.createTestTrip(member);
		String startDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getStartDate());
		String endDateTimeStr = DateTimeUtil.toString(expectedTrip.getTripSchedule().getEndDate());

		TripRequest createTripRequest = TripRequest.builder()
			.name(expectedTrip.getName())
			.tripType(expectedTrip.getTripType().getValue())
			.startDate(startDateTimeStr)
			.endDate(endDateTimeStr)
			.build();

		// when (trip)
		ResultActions createTripAction = mockMvc.perform(post("/v1/trips")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(createTripRequest)));
		MvcResult result = createTripAction.andReturn();

		// then (trip)
		TripResponse responseTrip = objectMapper.readValue(result.getResponse().getContentAsString(),
			TripResponse.class);
		long expectedTripId = responseTrip.tripId();
		createTripAction.andExpect(status().isCreated());
		assertTripResponse(expectedTrip, createTripAction);

		// given (itinerary)
		ItineraryRequest createItineraryRequest = buildItineraryRequest(DateTimeScheduleInfo.builder()
			.startDateTime(LocalDateTime.from(expectedTrip.getTripSchedule().getStartDate().atStartOfDay()))
			.endDateTime(LocalDateTime.from(expectedTrip.getTripSchedule().getEndDate().atStartOfDay())).build());

		Itinerary expectedItinerary = ItineraryRequest.toEntity(createItineraryRequest, expectedTrip);

		// when (itinerary)
		ResultActions createItineraryAction = mockMvc.perform(post("/v1/trips/" + expectedTripId + "/itineraries")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(createItineraryRequest)));

		// then (itinerary)
		createItineraryAction.andExpect(status().isCreated());
		assertItineraryResponse(expectedItinerary, createItineraryAction);
	}

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
}
