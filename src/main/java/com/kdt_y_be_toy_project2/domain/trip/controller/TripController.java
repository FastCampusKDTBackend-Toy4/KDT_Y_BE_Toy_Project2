package com.kdt_y_be_toy_project2.domain.trip.controller;

import com.kdt_y_be_toy_project2.domain.trip.dto.GetTripResponse;
import com.kdt_y_be_toy_project2.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @GetMapping("/trips")
    public ResponseEntity<List<GetTripResponse>> getAllTrips() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tripService.getAllTrips());
    }

    @GetMapping("/trips/{trip_id}")
    public ResponseEntity<GetTripResponse> getTripById(
            @PathVariable(name = "trip_id") final Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tripService.getTripById(id));
    }
}
