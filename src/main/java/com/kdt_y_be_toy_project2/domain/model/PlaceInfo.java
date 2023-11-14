package com.kdt_y_be_toy_project2.domain.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PlaceInfo {

    private String name;
    private String roadAddressName;
    private String x;
    private String y;


    @Builder
    public PlaceInfo(String name, String roadAddressName, String x, String y) {
        this.name = name;
        this.roadAddressName = roadAddressName;
        this.x = x;
        this.y = y;
    }

    @Builder
    private PlaceInfo(String name) {
        this.name = name;
    }
}

