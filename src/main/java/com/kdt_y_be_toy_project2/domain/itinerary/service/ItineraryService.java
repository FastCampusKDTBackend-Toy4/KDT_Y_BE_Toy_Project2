package com.kdt_y_be_toy_project2.domain.itinerary.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdt_y_be_toy_project2.domain.itinerary.api.RoadAddressInfoAPI;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.AccommodationInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.MoveInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.StayInfo;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.TransportationType;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.AccommodationInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.MoveInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.request.StayInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.InvalidItineraryDateException;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.InvalidItineraryDurationException;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.ItineraryNotFoundException;
import com.kdt_y_be_toy_project2.domain.itinerary.repository.ItineraryRepository;
import com.kdt_y_be_toy_project2.domain.member.exception.InvalidAuthException;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.exception.TripNotFoundException;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.resolver.LoginInfo;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItineraryService {

	private final ItineraryRepository itineraryRepository;
	private final TripRepository tripRepository;
	private final RoadAddressInfoAPI roadAddressInfoAPI;

	@Transactional(readOnly = true)
	public List<ItineraryResponse> getAllItineraries(final Long tripId) {

		List<ItineraryResponse> itineraryResponses = tripRepository.findById(tripId)
			.orElseThrow(TripNotFoundException::new)
			.getItineraries()
			.stream()
			.map(ItineraryResponse::from)
			.toList();

		if (itineraryResponses.isEmpty()) {
			throw new ItineraryNotFoundException();
		}
		return itineraryResponses;
	}

	@Transactional(readOnly = true)
	public ItineraryResponse getItineraryById(final Long tripId, final Long itineraryId) {

		Trip retrivedTrip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
		return findItineraryInTrip(retrivedTrip, itineraryId)
			.map(ItineraryResponse::from)
			.orElseThrow();
	}

	public ItineraryResponse createItinerary(final LoginInfo loginInfo, final Long tripId,
		final ItineraryRequest request) {

		Trip retrivedTrip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);

		checkAuth(loginInfo, retrivedTrip);
		checkItineraryDuration(retrivedTrip, request);
		checkInvalidDate(request);

		//Itinerary savedItinerary = Optional.of(itineraryRepository.save(ItineraryRequest.toEntity(request, retrivedTrip))).orElseThrow();
		Itinerary itinerary = updateItineraryWithRoadAddresses(request, retrivedTrip);
		Itinerary savedItinerary = Optional.of(itineraryRepository.save(itinerary)).orElseThrow();

		retrivedTrip.getItineraries().add(savedItinerary);

		return Optional.of(ItineraryResponse.from(savedItinerary)).orElseThrow();
	}

	public ItineraryResponse editItinerary(final LoginInfo loginInfo, final Long tripId, final Long itineraryId,
		final ItineraryRequest request) {

		Trip retrivedTrip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);

		checkAuth(loginInfo, retrivedTrip);
		Itinerary retrivedItinerary = findItineraryInTrip(retrivedTrip, itineraryId).orElseThrow();
		Itinerary updateItinerary = updateItineraryWithRoadAddresses(request, retrivedTrip);
		retrivedItinerary.update(updateItinerary);
		checkItineraryDuration(retrivedItinerary.getTrip(), request);
		checkInvalidDate(request);

		return Optional.of(itineraryRepository.save(retrivedItinerary))
			.map(ItineraryResponse::from)
			.orElseThrow();
	}

	void checkItineraryDuration(Trip trip, ItineraryRequest itinerary) {
		LocalDateTime tripStartTime = trip.getTripSchedule().getStartDate().atStartOfDay();
		LocalDateTime tripEndTime = trip.getTripSchedule().getEndDate().atStartOfDay().plusDays(1);

		if (LocalDateTimeUtil.toLocalDateTime(itinerary.stayInfoRequest().startDateTime()).isBefore(tripStartTime) ||
			LocalDateTimeUtil.toLocalDateTime(itinerary.accommodationInfoRequest().startDateTime())
				.isBefore(tripStartTime) ||
			LocalDateTimeUtil.toLocalDateTime(itinerary.moveInfoRequest().startDateTime()).isBefore(tripStartTime) ||
			LocalDateTimeUtil.toLocalDateTime(itinerary.stayInfoRequest().endDateTime()).isAfter(tripEndTime) ||
			LocalDateTimeUtil.toLocalDateTime(itinerary.accommodationInfoRequest().endDateTime()).isAfter(tripEndTime)
			||
			LocalDateTimeUtil.toLocalDateTime(itinerary.moveInfoRequest().endDateTime()).isAfter(tripEndTime)
		)
			throw new InvalidItineraryDurationException();
	}

	void checkInvalidDate(ItineraryRequest itinerary) {

		if (LocalDateTimeUtil.toLocalDateTime(itinerary.moveInfoRequest().startDateTime())
			.isAfter(LocalDateTimeUtil.toLocalDateTime(itinerary.moveInfoRequest().endDateTime())) ||
			LocalDateTimeUtil.toLocalDateTime(itinerary.accommodationInfoRequest().startDateTime())
				.isAfter(LocalDateTimeUtil.toLocalDateTime(itinerary.accommodationInfoRequest().endDateTime())) ||
			LocalDateTimeUtil.toLocalDateTime(itinerary.stayInfoRequest().startDateTime())
				.isAfter(LocalDateTimeUtil.toLocalDateTime(itinerary.stayInfoRequest().endDateTime()))
		)
			throw new InvalidItineraryDateException();
	}

	Optional<Itinerary> findItineraryInTrip(Trip trip, long itineraryId) {

		for (Itinerary itinerary : trip.getItineraries()) {
			if (itinerary.getId().equals(itineraryId)) {
				return Optional.of(itinerary);
			}
		}

		throw new ItineraryNotFoundException();
	}

	void checkAuth(LoginInfo loginInfo, Trip trip) {
		if (!loginInfo.username().equals(trip.getMember().getEmail())) {
			throw new InvalidAuthException();
		}
	}

	public Itinerary updateItineraryWithRoadAddresses(ItineraryRequest request, Trip trip) {

		MoveInfoRequest moveInfoRequest = request.moveInfoRequest();
		AccommodationInfoRequest accommodationInfoRequest = request.accommodationInfoRequest();
		StayInfoRequest stayInfoRequest = request.stayInfoRequest();

		return Itinerary.builder()
			.stayInfo(buildStayInfo(stayInfoRequest))
			.accommodationInfo(buildAccommodationInfo(accommodationInfoRequest))
			.moveInfo(buildMoveInfo(moveInfoRequest))
			.trip(trip)
			.build();
	}

	MoveInfo buildMoveInfo(MoveInfoRequest moveInfoRequest) {
		return MoveInfo.builder()
			.moveSchedule(DateTimeScheduleInfo.builder()
				.startDateTime(LocalDateTimeUtil.toLocalDateTime(moveInfoRequest.startDateTime()))
				.endDateTime(LocalDateTimeUtil.toLocalDateTime(moveInfoRequest.endDateTime()))
				.build())
			.sourcePlaceInfo(roadAddressInfoAPI.getPlaceInfoByKeyword(moveInfoRequest.sourcePlaceName()))
			.destPlaceInfo(roadAddressInfoAPI.getPlaceInfoByKeyword(moveInfoRequest.destPlaceName()))
			.transportationType(TransportationType.getByValue(moveInfoRequest.transportationType()))
			.build();
	}

	StayInfo buildStayInfo(StayInfoRequest stayInfoRequest) {
		return StayInfo.builder()
			.staySchedule(DateTimeScheduleInfo.builder()
				.startDateTime(LocalDateTimeUtil.toLocalDateTime(stayInfoRequest.startDateTime()))
				.endDateTime(LocalDateTimeUtil.toLocalDateTime(stayInfoRequest.endDateTime()))
				.build())
			.stayPlaceInfo(roadAddressInfoAPI.getPlaceInfoByKeyword(stayInfoRequest.stayPlaceName()))
			.build();
	}

	AccommodationInfo buildAccommodationInfo(AccommodationInfoRequest accommodationInfoRequest) {
		return AccommodationInfo.builder()
			.accommodationSchedule(DateTimeScheduleInfo.builder()
				.startDateTime(LocalDateTimeUtil.toLocalDateTime(accommodationInfoRequest.startDateTime()))
				.endDateTime(LocalDateTimeUtil.toLocalDateTime(accommodationInfoRequest.endDateTime()))
				.build())
			.accommodationPlaceInfo(
				roadAddressInfoAPI.getPlaceInfoByKeyword(accommodationInfoRequest.accommodationPlaceName()))
			.build();
	}

}
