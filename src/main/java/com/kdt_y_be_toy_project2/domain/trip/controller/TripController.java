package com.kdt_y_be_toy_project2.domain.trip.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdt_y_be_toy_project2.domain.trip.dto.TripRequest;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripResponse;
import com.kdt_y_be_toy_project2.domain.trip.service.TripService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/trips")
@RequiredArgsConstructor
public class TripController {
	private final TripService tripService;

	@GetMapping
	public ResponseEntity<List<TripResponse>> getAllTrips() {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(tripService.getAllTrips());
	}

	@GetMapping("/{trip_id}")
	public ResponseEntity<TripResponse> getTripById(
		@PathVariable(name = "trip_id") final Long tripId
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(tripService.getTripById(tripId));
	}

	@PostMapping
	public ResponseEntity<TripResponse> createTrip(
		@Valid @RequestBody final TripRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.contentType(MediaType.APPLICATION_JSON)
			.body(tripService.createTrip(request));
	}

	@PutMapping("/{trip_id}")
	public ResponseEntity<TripResponse> editTrip(
		@PathVariable(name = "trip_id") final Long tripId,
		@Valid @RequestBody final TripRequest request
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(tripService.editTrip(tripId, request));
	}
}
