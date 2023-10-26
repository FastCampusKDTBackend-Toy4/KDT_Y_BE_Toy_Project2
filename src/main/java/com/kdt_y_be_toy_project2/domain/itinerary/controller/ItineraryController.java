package com.kdt_y_be_toy_project2.domain.itinerary.controller;


import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.service.ItineraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService;

    @GetMapping("/trips/{trip_id}/itineraries")
    public ResponseEntity<List<ItineraryResponse>> getAllItineraries(
            @PathVariable(name = "trip_id") final Long tripId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(itineraryService.getAllItineraries(tripId));
    }

    @GetMapping("/trips/{trip_id}/itineraries/{itinerary_id}")
    public ResponseEntity<ItineraryResponse> getItineraryByTripId(
            @PathVariable(name = "trip_id") final Long tripId,
            @PathVariable(name = "itinerary_id") final Long itineraryId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(itineraryService.getItineraryById(tripId, itineraryId));
    }

    @PostMapping("/trips/{trip_id}/itineraries")
    public ResponseEntity<ItineraryResponse> createItinerary(
            @PathVariable(name = "trip_id") final Long tripId,
            @RequestBody final ItineraryRequest request
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(itineraryService.createItinerary(tripId, request));
    }

    @PutMapping("/trips/{trip_id}/itineraries/{itinerary_id}")
    public ResponseEntity<ItineraryResponse> editItinerary(
            @PathVariable(name = "trip_id") final Long tripId,
            @PathVariable(name = "itinerary_id") final Long itinerary_id,
            @RequestBody final ItineraryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(itineraryService.editItinerary(itinerary_id, request));
    }
}
