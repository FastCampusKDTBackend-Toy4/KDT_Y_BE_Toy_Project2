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
import com.kdt_y_be_toy_project2.global.error.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "여행", description = "여행 관련 api입니다.")
@RestController
@RequestMapping("/v1/trips")
@RequiredArgsConstructor
public class TripController {
	private final TripService tripService;

	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "여행 리스트 조회 성공",
			content = @Content(schema = @Schema(implementation = TripResponse.class))),
		@ApiResponse(responseCode = "404", description = "여행 리스트 조회 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@Operation(summary = "여행 리스트 조회", description = "모든 여행 리스트를 조회하는 메소드입니다.")
	@GetMapping
	public ResponseEntity<List<TripResponse>> getAllTrips() {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(tripService.getAllTrips());
	}

	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "여행 조회 성공",
			content = @Content(schema = @Schema(implementation = TripResponse.class))),
		@ApiResponse(responseCode = "404", description = "여행 조회 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@Operation(summary = "여행 조회", description = "하나의 여행을 조회하는 메소드입니다.")
	@GetMapping("/{trip_id}")
	public ResponseEntity<TripResponse> getTripById(
		@Parameter(description = "여행 ID", required = true, example = "1")
		@PathVariable(name = "trip_id") final Long tripId
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(tripService.getTripById(tripId));
	}

	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "여행 등록 성공",
			content = @Content(schema = @Schema(implementation = TripResponse.class))),
		@ApiResponse(responseCode = "404", description = "여행 등록 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@Operation(summary = "여행 등록", description = "하나의 여행을 등록하는 메소드입니다.")
	@PostMapping
	public ResponseEntity<TripResponse> createTrip(
		@Valid @RequestBody final TripRequest request
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(tripService.createTrip(request));
	}

	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "여행 수정 성공",
			content = @Content(schema = @Schema(implementation = TripResponse.class))),
		@ApiResponse(responseCode = "404", description = "여행 수정 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@Operation(summary = "여행 수정", description = "하나의 여행을 수정하는 메소드입니다.")
	@PutMapping("/{trip_id}")
	public ResponseEntity<TripResponse> editTrip(
		@Parameter(description = "여행 ID", required = true, example = "1")
		@PathVariable(name = "trip_id") final Long tripId,
		@Valid @RequestBody final TripRequest request
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(tripService.editTrip(tripId, request));
	}
}
