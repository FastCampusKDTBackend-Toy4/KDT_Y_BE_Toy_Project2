package com.kdt_y_be_toy_project2.domain.itinerary.controller;


import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.service.ItineraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kdt_y_be_toy_project2.global.error.ErrorResponse;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "여정 ", description = "여정 관련 api입니다.")
public class ItineraryController {
    private final ItineraryService itineraryService;

    @GetMapping("/trips/{trip_id}/itineraries")
    @Operation(summary = "여정 리스트 조회", description = "특정 여행과 관련된 모든 여정을 조회합니다.", parameters = {})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 여정 조회 성공", content = @Content(schema = @Schema(implementation = ItineraryResponse.class))),
            @ApiResponse(responseCode = "404", description = "여행 ID와 매칭되는 여행이 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })

    public ResponseEntity<List<ItineraryResponse>> getAllItineraries(
            @Parameter(description = "여행 ID") @PathVariable(name = "trip_id") final Long tripId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(itineraryService.getAllItineraries(tripId));
    }

    @GetMapping("/trips/{trip_id}/itineraries/{itinerary_id}")
    @Operation(summary = "여정 조회", description = "특정 여행에 속한 특정 여정을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "여정 조회 성공", content = @Content(schema = @Schema(implementation = ItineraryResponse.class))),
            @ApiResponse(responseCode = "404", description = "여정 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ItineraryResponse> getItineraryByTripId(
            @Parameter(description = "여행 ID") @PathVariable(name = "trip_id") final Long tripId,
            @Parameter(description = "여정 ID") @PathVariable(name = "itinerary_id") final Long itineraryId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(itineraryService.getItineraryById(tripId, itineraryId));
    }

    @PostMapping("/trips/{trip_id}/itineraries")
    @Operation(summary = "여정 추가", description = "특정 여행에 여정을 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "여정 추가 성공",content = @Content(schema = @Schema(implementation = ItineraryResponse.class))),
            @ApiResponse(responseCode = "404", description = "여행 ID와 매칭되는 여행이 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "여행 일정 안에 여정이 들어갈 수 없습니다. (여정이 여행 일정 범위 밖에 있습니다.)", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    public ResponseEntity<ItineraryResponse> createItinerary(
            @Parameter(description = "여행 ID") @PathVariable(name = "trip_id") final Long tripId,
            @Parameter(description = "여정 요청 객체") @RequestBody final ItineraryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(itineraryService.createItinerary(tripId, request));
    }

    @PutMapping("/trips/{trip_id}/itineraries/{itinerary_id}")
    @Operation(summary = "여정 수정", description = "특정 여행에 속한 특정 여정을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "여정 수정 성공", content = @Content(schema = @Schema(implementation = ItineraryResponse.class))),
            @ApiResponse(responseCode = "404", description = "여정 수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ItineraryResponse> editItinerary(
            @Parameter(description = "여행 ID") @PathVariable(name = "trip_id") final Long tripId,
            @Parameter(description = "여정 ID") @PathVariable(name = "itinerary_id") final Long itineraryId,
            @Parameter(description = "여정 요청 객체") @RequestBody final ItineraryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(itineraryService.editItinerary(tripId, itineraryId, request));
    }
}
