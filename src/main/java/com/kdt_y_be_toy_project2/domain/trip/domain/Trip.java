package com.kdt_y_be_toy_project2.domain.trip.domain;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private TripType tripType;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Itinerary> itineraries;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDateTime", column = @Column(name = "start_date_time", nullable = false)),
            @AttributeOverride(name = "endDateTime", column = @Column(name = "end_date_time", nullable = false))
    })
    private TimeScheduleInfo tripSchedule;

    @Builder
    private Trip(Long id, String name, TripType tripType, TimeScheduleInfo tripSchedule) {
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
