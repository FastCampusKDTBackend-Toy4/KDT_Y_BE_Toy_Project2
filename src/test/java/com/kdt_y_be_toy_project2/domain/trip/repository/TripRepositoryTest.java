package com.kdt_y_be_toy_project2.domain.trip.repository;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.member.repository.MemberRepository;
import com.kdt_y_be_toy_project2.domain.trip.domain.Likes;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.id.LikesID;
import com.kdt_y_be_toy_project2.global.factory.MemberTestFactory;
import com.kdt_y_be_toy_project2.global.factory.TripTestFactory;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TripRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	private TripRepository tripRepository;
	@Autowired
	private LikesRepository likesRepository;

	@DisplayName("여행 정보를 등록할 수 있다.")
	@Test
	public void saveTripSuccessTest() {
		// given
		Member member = memberRepository.save(MemberTestFactory.createTestMemberWithRandomPassword());
		Trip trip = TripTestFactory.createTestTrip(member);

		// when
		Trip savedTrip = tripRepository.save(trip);

		// then
		Assertions.assertEquals(trip, savedTrip);
	}

	@DisplayName("여행에 좋아요를 눌러서 좋아요 정보를 저장할 수 있다.")
	@Test
	public void saveLikesSuccessTest() {
		// given
		Member member = memberRepository.save(MemberTestFactory.createTestMemberWithRandomPassword());
		Trip trip = tripRepository.save(TripTestFactory.createTestTrip(member));

		Likes likes = Likes.builder()
			.likesID(LikesID.builder()
				.tripId(trip.getId())
				.memberEmail(member.getEmail())
				.build())
			.trip(trip)
			.member(member)
			.build();

		// when
		Likes savedLikes = likesRepository.save(likes);

		// then
		Assertions.assertEquals(likes.getLikesID().getTripId(), savedLikes.getLikesID().getTripId());
		Assertions.assertEquals(likes.getLikesID().getMemberEmail(), savedLikes.getLikesID().getMemberEmail());
	}

	@DisplayName("내가 눌렀던 좋아요 여행 리스트를 가져올 수 있다.")
	@Test
	public void getLikesCountSuccessTest() {
		// given
		Member member = memberRepository.save(MemberTestFactory.createTestMemberWithRandomPassword());
		Trip trip = tripRepository.save(TripTestFactory.createTestTrip(member));

		Likes likes = Likes.builder()
			.likesID(LikesID.builder()
				.tripId(trip.getId())
				.memberEmail(member.getEmail())
				.build())
			.trip(trip)
			.member(member)
			.build();
		likesRepository.save(likes);

		// when
		List<Trip> tripList = likesRepository.getAllTripByMemberEmail(member.getEmail());

		// then
		Assertions.assertEquals(tripList.size(), 1);
	}
}