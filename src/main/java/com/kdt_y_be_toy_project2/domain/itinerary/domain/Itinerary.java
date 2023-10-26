package com.kdt_y_be_toy_project2.domain.itinerary.domain;


import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Itinerary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="trip")
    private Trip trip;

    //체류 정보, 이동 정보, 숙소 정보
    @Embedded
    private StayInfo stayInfo;

    @Embedded
    private MoveInfo moveInfo;

    @Embedded
    private AccommodationInfo accommodationInfo;

    public Itinerary update(Itinerary itinerary) {
        this.stayInfo = itinerary.stayInfo;
        this.moveInfo = itinerary.moveInfo;
        this.accommodationInfo = itinerary.accommodationInfo;
        return this;
    }
}