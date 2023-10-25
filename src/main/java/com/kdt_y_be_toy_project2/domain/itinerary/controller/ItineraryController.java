package com.kdt_y_be_toy_project2.domain.itinerary.controller;


import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryDTO;
import com.kdt_y_be_toy_project2.domain.itinerary.service.ItineraryService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<ItineraryDTO>> getAllItineraries(
            @PathVariable(name = "trip_id") final Long tripId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(itineraryService.getAllItineraries(tripId));
    }

    @GetMapping("/trips/{trip_id}/itineraries/{itinerary_id}")
    public ResponseEntity<ItineraryDTO> getItineraryByTripId(
            @PathVariable(name = "trip_id") final Long tripId,
            @PathVariable(name = "itinerary_id") final Long itineraryId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(itineraryService.getItineraryById(tripId, itineraryId));
    }

    @PostMapping("/trips/{trip_id}/itineraries")
    public ResponseEntity<ItineraryDTO> createItinerary(
            @PathVariable(name = "trip_id") final Long tripId,
            @RequestBody final ItineraryDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(itineraryService.createItinerary(tripId, request));
    }

    @PutMapping("/trips/{trip_id}/itineraries/{itinerary_id}")
    public ResponseEntity<ItineraryDTO> editItinerary(
            @PathVariable(name = "trip_id") final Long tripId,
            @PathVariable(name = "itinerary_id") final Long itinerary_id,
            @RequestBody final ItineraryDTO request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(itineraryService.editItinerary(itinerary_id, request));
    }
}
