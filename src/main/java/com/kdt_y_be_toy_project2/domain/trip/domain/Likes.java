package com.kdt_y_be_toy_project2.domain.trip.domain;

import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.trip.domain.id.LikesID;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Likes {

	@EmbeddedId
	private LikesID likesID;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("trip_id")
	private Trip trip;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("member_email")
	private Member member;

	@Builder
	private Likes(LikesID likesID, Trip trip, Member member) {
		this.likesID = likesID;
		this.trip = trip;
		this.member = member;
		trip.increaseCount();
	}
}
