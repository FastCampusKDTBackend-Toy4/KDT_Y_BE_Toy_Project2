package com.kdt_y_be_toy_project2.domain.trip.domain.id;

import java.io.Serializable;

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
public class LikesID implements Serializable {

	private Long trip;
	private String member;

	@Builder
	private LikesID(Long trip, String member) {
		this.trip = trip;
		this.member = member;
	}
}
