package com.kdt_y_be_toy_project2.domain.itinerary.domain;

import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import com.kdt_y_be_toy_project2.domain.model.DateTimeScheduleInfo;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class MoveInfo {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDateTime", column = @Column(name = "move_start_date_time")),
            @AttributeOverride(name = "endDateTime", column = @Column(name = "move_end_date_time"))
    })
    private DateTimeScheduleInfo moveSchedule;


    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "source_place_name")),
            @AttributeOverride(name = "roadAddressName", column = @Column(name = "source_place_road_address_name")),
            @AttributeOverride(name = "x", column = @Column(name = "source_place_x")),
            @AttributeOverride(name = "y", column = @Column(name = "source_place_y")),
    })
    private PlaceInfo sourcePlaceInfo;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "dest_place_name")),
            @AttributeOverride(name = "roadAddressName", column = @Column(name = "dest_place_road_address_name")),
            @AttributeOverride(name = "x", column = @Column(name = "dest_place_x")),
            @AttributeOverride(name = "y", column = @Column(name = "dest_place_y")),
    })
    private PlaceInfo destPlaceInfo;


    @Column(name = "transportation_type")
    @Enumerated(EnumType.STRING)
    private TransportationType transportationType;



}
