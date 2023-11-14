package com.kdt_y_be_toy_project2.domain.trip.domain.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class LikesID {

	@Column(name = "trip_id")
	private Long tripId;

	@Column(name = "member_email")
	private String memberEmail;

	@Builder
	private LikesID(Long tripId, String memberEmail) {
		this.tripId = tripId;
		this.memberEmail = memberEmail;
	}
}
