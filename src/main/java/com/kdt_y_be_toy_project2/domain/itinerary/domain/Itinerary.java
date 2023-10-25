package com.kdt_y_be_toy_project2.domain.itinerary.domain;

import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryDTO;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Itinerary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name="tripId")
//    private Trip tripId;

    //체류 정보, 이동 정보, 숙소 정보
    @Embedded
    private StayInfo stayInfo;

    @Embedded
    private MoveInfo moveInfo;

    @Embedded
    private AccommodationInfo accommodationInfo;

    public static Itinerary to(ItineraryDTO itineraryDTO) {
        return Itinerary.builder()
                .id(itineraryDTO.id())
                .stayInfo(StayInfo.to(itineraryDTO.stayInfoDTO()))
                .moveInfo(MoveInfo.to(itineraryDTO.moveInfoDTO()))
                .accommodationInfo(AccommodationInfo.to(itineraryDTO.accommodationInfoDTO()))
                .build();
    }
}