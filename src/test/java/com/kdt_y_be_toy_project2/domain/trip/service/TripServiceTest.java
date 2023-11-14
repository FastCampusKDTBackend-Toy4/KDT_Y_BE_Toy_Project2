package com.kdt_y_be_toy_project2.domain.trip.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kdt_y_be_toy_project2.domain.model.DateScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.type.TripType;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripRequest;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripResponse;
import com.kdt_y_be_toy_project2.domain.trip.exception.TripNotFoundException;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.error.ErrorCode;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

	@Mock
	private TripRepository tripRepository;

	@InjectMocks
	private TripService tripService;

	private Trip trip, trip2;
	private TripRequest createRequest, editRequest;

	@BeforeEach
	public void init() {
		trip = Trip.builder()
			.id(1L)
			.name("Test Trip")
			.tripType(TripType.DOMESTIC)
			.tripSchedule(
				DateScheduleInfo.builder()
					.startDate(LocalDate.of(2023, 10, 25))
					.endDate(LocalDate.of(2023, 10, 27))
					.build()
			)
			.build();
		trip2 = Trip.builder()
			.name("Test Trip2")
			.tripType(TripType.FOREIGN)
			.tripSchedule(
				DateScheduleInfo.builder()
					.startDate(LocalDate.of(2023, 10, 26))
					.endDate(LocalDate.of(2023, 10, 27))
					.build()
			)
			.build();
		createRequest = new TripRequest("Test Trip", "2023-10-25", "2023-10-27", "국내");
		editRequest = new TripRequest("Test Trip3", "2023-11-11", "2023-11-24", "국외");
	}

	@DisplayName("여행 전체 조회")
	@Test
	void getAllTrips() {
		// given
		given(tripRepository.findAll()).willReturn(List.of(trip, trip2));

		// when
		List<TripResponse> trips = tripService.getAllTrips();

		//then
		assertThat(trips).hasSize(2);
		assertThat(trips.get(1).tripName()).isEqualTo("Test Trip2");
	}

	@DisplayName("여행 전체 조회(데이터 없음)")
	@Test
	void getAllTripsEmpty() {
		// given
		given(tripRepository.findAll()).willReturn(new ArrayList<>());

		// when & then
		assertThatThrownBy(() -> tripService.getAllTrips())
			.isInstanceOf(TripNotFoundException.class)
			.hasMessage(ErrorCode.TRIP_NOT_FOUND.getSimpleMessage());
	}

	@DisplayName("여행 ID 조회")
	@Test
	void getTripById() {
		// given
		given(tripRepository.findById(anyLong())).willReturn(Optional.ofNullable(trip2));

		// when
		TripResponse tripResponse = tripService.getTripById(anyLong());

		// then
		assertThat(tripResponse.tripName()).isEqualTo("Test Trip2");
	}

	@DisplayName("여행 ID 조회 실패")
	@Test
	void getTripByIdIsNull() {
		// given
		given(tripRepository.findById(anyLong())).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> tripService.getTripById(anyLong())).isInstanceOf(TripNotFoundException.class)
			.hasMessage(ErrorCode.TRIP_NOT_FOUND.getSimpleMessage());
	}

	@DisplayName("여행 기록")
	@Test
	void createTrip() {
		// given
		given(tripRepository.save(any(Trip.class))).willReturn(trip);

		// when
		TripResponse tripResponse = tripService.createTrip(createRequest);

		// then
		assertThat(tripResponse.tripName()).isEqualTo("Test Trip");
		assertThat(tripResponse.tripType()).isEqualTo("국내");
		verify(tripRepository, times(1)).save(any(Trip.class));
	}

	@DisplayName("여행 수정")
	@Test
	void editTrip() {
		// given
		given(tripRepository.findById(anyLong())).willReturn(Optional.ofNullable(trip));

		// when
		TripResponse tripResponse = tripService.editTrip(anyLong(), editRequest);

		// then
		assertThat(tripResponse.tripName()).isEqualTo("Test Trip3");
		assertThat(tripResponse.startDate()).isEqualTo("2023-11-11");
		assertThat(tripResponse.endDate()).isEqualTo("2023-11-24");
		assertThat(tripResponse.tripType()).isEqualTo("국외");
	}
}
