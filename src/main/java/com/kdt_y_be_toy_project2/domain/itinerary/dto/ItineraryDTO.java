package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
public record ItineraryDTO (Long id, StayInfoDTO stayInfoDTO, MoveInfoDTO moveInfoDTO, AccommodationInfoDTO accommodationInfoDTO) {
    public static ItineraryDTO from(final Itinerary itinerary) {
        return ItineraryDTO.builder()
                .id(itinerary.getId())
                .stayInfoDTO(StayInfoDTO.from(itinerary.getStayInfo()))
                .moveInfoDTO(MoveInfoDTO.from(itinerary.getMoveInfo()))
                .accommodationInfoDTO(AccommodationInfoDTO.from(itinerary.getAccommodationInfo()))
                .build();
    }

}
