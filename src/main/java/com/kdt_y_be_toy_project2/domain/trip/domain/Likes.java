package com.kdt_y_be_toy_project2.domain.trip.domain;

import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.trip.domain.id.LikesID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@IdClass(LikesID.class)
public class Likes {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	private Trip trip;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@Builder
	private Likes(Trip trip, Member member) {
		this.trip = trip;
		this.member = member;
	}
}
