package com.kdt_y_be_toy_project2.domain.itinerary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.StayInfo;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Builder
public record StayInfoDTO(
        @NotNull(message = "휴식 시작 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startDateTime,

        @NotNull(message = "휴식 종료 시간을 입력해야 합니다. (예: 2023-10-25 12:00)")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endDateTime,

        PlaceInfo stayPlaceInfo
) {
    public static StayInfoDTO from(final StayInfo stayInfo) {
        return StayInfoDTO.builder()
                .startDateTime(stayInfo.getStaySchedule().getStartDateTime())
                .endDateTime(stayInfo.getStaySchedule().getEndDateTime())
                .stayPlaceInfo(stayInfo.getStayPlaceInfo())
                .build();
    }
}
