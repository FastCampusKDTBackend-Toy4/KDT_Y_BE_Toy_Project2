package com.kdt_y_be_toy_project2.domain.itinerary.domain;

import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "itinerary")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Itinerary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    //체류 정보, 이동 정보, 숙소 정보
    @Embedded
    private StayInfo stayInfo;

    @Embedded
    private MoveInfo moveInfo;

    @Embedded
    private AccommodationInfo accommodationInfo;


}