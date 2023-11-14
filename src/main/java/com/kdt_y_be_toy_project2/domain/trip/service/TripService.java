package com.kdt_y_be_toy_project2.domain.trip.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdt_y_be_toy_project2.domain.member.repository.MemberRepository;
import com.kdt_y_be_toy_project2.domain.trip.domain.Likes;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.id.LikesID;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripRequest;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripResponse;
import com.kdt_y_be_toy_project2.domain.trip.exception.TripAlreadyLikesException;
import com.kdt_y_be_toy_project2.domain.trip.exception.TripNotFoundException;
import com.kdt_y_be_toy_project2.domain.trip.repository.LikesRepository;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TripService {

	private final TripRepository tripRepository;
	private final MemberRepository memberRepository;
	private final LikesRepository likesRepository;

	@Transactional(readOnly = true)
	public List<TripResponse> getAllTrips() {
		List<Trip> trips = tripRepository.findAll();

		if (trips.isEmpty()) {
			throw new TripNotFoundException();
		}
		return trips
			.stream().map(TripResponse::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public TripResponse getTripById(final Long tripId) {
		return tripRepository.findById(tripId)
			.map(TripResponse::from)
			.orElseThrow(TripNotFoundException::new);
	}

	public TripResponse createTrip(final TripRequest request) {
		return Optional.of(tripRepository.save(TripRequest.toEntity(request)))
			.map(TripResponse::from)
			.orElseThrow();
	}

	public TripResponse editTrip(final Long tripId, final TripRequest request) {
		Trip updatedTrip = tripRepository.findById(tripId)
			.map(trip -> trip.update(TripRequest.toEntity(request)))
			.orElseThrow(TripNotFoundException::new);

		return TripResponse.from(updatedTrip);
	}

	public List<TripResponse> getMemberLikedTrip(String memberEmail) {
		return likesRepository.getAllTripByMemberEmail(memberEmail)
			.stream().map(TripResponse::from).toList();
	}

	public void addLikeTrip(Long tripId, String memberEmail) {
		Trip trip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);

		LikesID newLikesID = LikesID.builder()
			.tripId(tripId)
			.memberEmail(memberEmail)
			.build();

		if (likesRepository.existsById(newLikesID)) {
			throw new TripAlreadyLikesException();
		}

		Likes newLikes = Likes.builder()
			.likesID(newLikesID)
			.trip(trip)
			.build();
		likesRepository.save(newLikes);
	}
}
