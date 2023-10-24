package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@AllArgsConstructor (access = AccessLevel.PROTECTED)
@Setter
public class ItineraryDTO {

    @NotNull
    private Long id;

    @NotNull
    private StayInfoDTO stayInfoDTO;

    @NotNull
    private MoveInfoDTO moveInfoDTO;

    @NotNull
    private AccommodationInfoDTO accommodationInfoDTO;

    public static ItineraryDTO from(final Itinerary itinerary) {
        return ItineraryDTO.builder()
                .id(itinerary.getId())
                .stayInfoDTO(StayInfoDTO.from(itinerary.getStayInfo()))
                .moveInfoDTO(MoveInfoDTO.from(itinerary.getMoveInfo()))
                .accommodationInfoDTO(AccommodationInfoDTO.from(itinerary.getAccommodationInfo()))
                .build();
    }

}
