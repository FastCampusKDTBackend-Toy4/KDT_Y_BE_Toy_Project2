package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.StayInfo;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.TimeScheduleInfo;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Builder
public record StayInfoDTO (TimeScheduleInfo staySchedule, PlaceInfo stayPlaceInfo){
    public static StayInfoDTO from(final StayInfo stayInfo){
        return StayInfoDTO.builder()
                .staySchedule(stayInfo.getStaySchedule())
                .stayPlaceInfo(stayInfo.getStayPlaceInfo())
                .build();
    }
}
