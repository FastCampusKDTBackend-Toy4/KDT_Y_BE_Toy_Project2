package com.kdt_y_be_toy_project2.domain.itinerary.domain;

public enum TransportationType {
    //도보, 자동차, 대중교통
    WALK("WALK"), CAR("CAR"), PUBLIC_TRANSPORT("PUBLIC_TRANSPORT");

    String value;

    TransportationType(String value){
        value = value.toUpperCase();
    }
}
