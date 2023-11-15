package com.kdt_y_be_toy_project2.domain.trip.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.member.repository.MemberRepository;
import com.kdt_y_be_toy_project2.domain.model.exception.InvalidDateRangeException;
import com.kdt_y_be_toy_project2.domain.trip.domain.Comment;
import com.kdt_y_be_toy_project2.domain.trip.domain.Likes;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.id.LikesID;
import com.kdt_y_be_toy_project2.domain.trip.dto.FindTripResponse;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripCommentRequest;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripRequest;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripResponse;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripSearchRequest;
import com.kdt_y_be_toy_project2.domain.trip.exception.TripAlreadyLikesException;
import com.kdt_y_be_toy_project2.domain.trip.exception.TripNotFoundException;
import com.kdt_y_be_toy_project2.domain.trip.exception.TripSearchIllegalArgumentException;
import com.kdt_y_be_toy_project2.domain.trip.repository.CommentRepository;
import com.kdt_y_be_toy_project2.domain.trip.repository.LikesRepository;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.resolver.LoginInfo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TripService {

	private final TripRepository tripRepository;
	private final LikesRepository likesRepository;
	private final MemberRepository memberRepository;
	private final CommentRepository commentRepository;

	@Transactional(readOnly = true)
	public List<TripResponse> getAllTrips() {
		List<Trip> trips = tripRepository.findAll();

		if (trips.isEmpty()) {
			throw new TripNotFoundException();
		}
		return trips.stream().map(TripResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public FindTripResponse getTripById(final Long tripId) {
		Trip trip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
		List<Map<String, Object>> commentInfos = trip.getComments().stream()
			.map(comment -> {
				Map<String, Object> commentInfo = new HashMap<>();
				commentInfo.put("content", comment.getContent());
				commentInfo.put("email", comment.getMember().getEmail());
				commentInfo.put("createDateTime", comment.getCreatedDateTime());

				return commentInfo;
			}).toList();

		return FindTripResponse.from(trip, commentInfos);
	}

	public TripResponse createTrip(final TripRequest request, LoginInfo loginInfo) {
		Member member = memberRepository.findById(loginInfo.username()).orElseThrow(NullPointerException::new);
		return Optional.of(tripRepository.save(TripRequest.toEntity(request, member)))
			.map(TripResponse::from)
			.orElseThrow();
	}

	public TripResponse editTrip(final Long tripId, final TripRequest request, LoginInfo loginInfo) {
		Member member = memberRepository.findById(loginInfo.username()).orElseThrow(NullPointerException::new);
		return TripResponse.from(tripRepository.findById(tripId)
			.map(trip -> trip.update(TripRequest.toEntity(request, member)))
			.orElseThrow(TripNotFoundException::new));
	}

	@Transactional(readOnly = true)
	public List<TripResponse> searchTrips(TripSearchRequest request) {
		validateSearchTripRequest(request);
		return tripRepository.searchTrips(
				request.name(),
				request.getTripType(),
				request.getStartDate(),
				request.getEndDate()
			)
			.stream().map(TripResponse::from)
			.toList();
	}

	private void validateSearchTripRequest(TripSearchRequest request) {
		if (request.isAllNull()) {
			throw new TripSearchIllegalArgumentException();
		}

		if (isAllValidDate(request.getStartDate(), request.getEndDate())) {
			validateDateRange(
				Objects.requireNonNull(request.getStartDate()),
				Objects.requireNonNull(request.getEndDate())
			);
		}
	}

	private boolean isAllValidDate(LocalDate start, LocalDate end) {
		return start != null && end != null;
	}

	private void validateDateRange(LocalDate start, LocalDate end) {
		if (start.isAfter(end) && isDayTrip(start, end)) {
			throw new InvalidDateRangeException("startDate 는 항상 endDate 보다 과거여야 합니다. "
				+ "{\"startDate = \" %s / endDate = %s}".formatted(start, end));
		}
	}

	private boolean isDayTrip(LocalDate start, LocalDate end) {
		return !start.equals(end);
	}

	@Transactional(readOnly = true)
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

	public void createComment(TripCommentRequest dto, Long tripId, String email) {
		Trip trip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
		Member member = memberRepository.findByEmail(email).orElseThrow();
		Comment comment = Comment.builder()
			.trip(trip)
			.member(member)
			.content(dto.content())
			.build();
		commentRepository.save(comment);
	}
}
