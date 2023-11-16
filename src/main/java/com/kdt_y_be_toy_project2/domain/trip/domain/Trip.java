package com.kdt_y_be_toy_project2.domain.trip.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.model.DateScheduleInfo;
import com.kdt_y_be_toy_project2.domain.trip.domain.type.TripType;
import com.kdt_y_be_toy_project2.domain.trip.domain.type.TripTypeConverter;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Trip {
	@OneToMany(mappedBy = "trip", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<Comment> comments;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "member_email", nullable = false)
	private Member member;
	@Column(nullable = false)
	private String name;
	@Column(name = "trip_type", nullable = false)
	@Convert(converter = TripTypeConverter.class)
	private TripType tripType;
	@OneToMany(mappedBy = "trip", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<Itinerary> itineraries;
	@Column(nullable = false)
	private Long likesCount;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "startDate", column = @Column(name = "start_date", nullable = false)),
		@AttributeOverride(name = "endDate", column = @Column(name = "end_date", nullable = false))
	})
	private DateScheduleInfo tripSchedule;

	@Builder
	private Trip(Long id, Member member, String name, TripType tripType, DateScheduleInfo tripSchedule,
		List<Comment> comments, List<Itinerary> itineraries) {
		this.id = id;
		this.member = member;
		this.name = name;
		this.tripType = tripType;
		this.tripSchedule = tripSchedule;
		this.comments = Objects.requireNonNullElse(comments, new ArrayList<>());
		this.itineraries = Objects.requireNonNullElse(itineraries, new ArrayList<>());
		this.likesCount = 0L;
	}

	public Trip update(Trip trip) {
		this.name = trip.name;
		this.tripType = trip.tripType;
		this.tripSchedule = trip.tripSchedule;
		return this;
	}

	public void increaseCount() {
		this.likesCount++;
	}
}
