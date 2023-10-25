package com.kdt_y_be_toy_project2.domain.trip.service;

import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.dto.*;
import com.kdt_y_be_toy_project2.domain.trip.exception.TripNotFoundException;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    @Transactional(readOnly = true)
    public List<GetTripResponse> getAllTrips() {
        return tripRepository.findAll()
                .stream().map(GetTripResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GetTripResponse getTripById(final Long tripId) {
        return tripRepository.findById(tripId)
                .map(GetTripResponse::from)
                .orElseThrow(TripNotFoundException::new);
    }

    public CreateTripResponse createTrip(final CreateTripRequest request) {
        Trip newTrip = CreateTripRequest.toEntity(request);
        return Optional.of(tripRepository.save(newTrip))
                .map(CreateTripResponse::from)
                .orElseThrow();
    }

    public EditTripResponse editTrip(final Long tripId, final EditTripRequest request) {
        Trip updatedTrip = tripRepository.findById(tripId)
                .map(trip -> trip.update(EditTripRequest.toEntity(request)))
                .orElseThrow(TripNotFoundException::new);
        return Optional.of(tripRepository.save(updatedTrip))
                .map(EditTripResponse::from)
                .orElseThrow();
    }
}



