package com.kdt_y_be_toy_project2.domain.trip.controller;

import com.kdt_y_be_toy_project2.domain.trip.dto.*;
import com.kdt_y_be_toy_project2.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable(name = "trip_id") final Long tripId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tripService.getTripById(tripId));
    }

    @PostMapping("/trips")
    public ResponseEntity<CreateTripResponse> createTrip(
            @RequestBody final CreateTripRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tripService.createTrip(request));
    }

    @PutMapping("/trips/{trip_id}")
    public ResponseEntity<EditTripResponse> editTrip(
            @RequestBody final EditTripRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tripService.editTrip(request));
    }
}
