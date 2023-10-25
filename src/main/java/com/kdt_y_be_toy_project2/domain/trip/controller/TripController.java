package com.kdt_y_be_toy_project2.domain.trip.controller;

import com.kdt_y_be_toy_project2.domain.trip.dto.*;
import com.kdt_y_be_toy_project2.domain.trip.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/trips")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @GetMapping
    public ResponseEntity<List<GetTripResponse>> getAllTrips() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tripService.getAllTrips());
    }

    @GetMapping("/{trip_id}")
    public ResponseEntity<GetTripResponse> getTripById(
            @PathVariable(name = "trip_id") final Long tripId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tripService.getTripById(tripId));
    }

    @PostMapping
    public ResponseEntity<CreateTripResponse> createTrip(
            @Valid @RequestBody final CreateTripRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tripService.createTrip(request));
    }

    @PutMapping("/{trip_id}")
    public ResponseEntity<EditTripResponse> editTrip(
            @PathVariable(name = "trip_id") final Long tripId,
            @Valid @RequestBody final EditTripRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tripService.editTrip(tripId, request));
    }
}
