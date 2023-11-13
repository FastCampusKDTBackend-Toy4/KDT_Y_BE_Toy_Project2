package com.kdt_y_be_toy_project2.domain.trip.domain;

import java.util.ArrayList;
import java.util.List;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.model.DateScheduleInfo;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Trip {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(name = "trip_type", nullable = false)
	@Convert(converter = TripTypeConverter.class)
	private TripType tripType;

	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Itinerary> itineraries;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "startDate", column = @Column(name = "start_date", nullable = false)),
		@AttributeOverride(name = "endDate", column = @Column(name = "end_date", nullable = false))
	})
	private DateScheduleInfo tripSchedule;

	@Builder
	private Trip(Long id, String name, TripType tripType, DateScheduleInfo tripSchedule) {
		this.id = id;
		this.name = name;
		this.tripType = tripType;
		this.tripSchedule = tripSchedule;
		this.itineraries = new ArrayList<>();
	}

	public Trip update(Trip trip) {
		this.name = trip.name;
		this.tripType = trip.tripType;
		this.tripSchedule = trip.tripSchedule;
		return this;
	}
}
