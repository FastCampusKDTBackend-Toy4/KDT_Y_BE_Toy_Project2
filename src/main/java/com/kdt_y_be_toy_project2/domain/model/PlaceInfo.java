package com.kdt_y_be_toy_project2.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PlaceInfo {

    private String name;

    @Builder
    private PlaceInfo(String name) {
        this.name = name;
    }
}
