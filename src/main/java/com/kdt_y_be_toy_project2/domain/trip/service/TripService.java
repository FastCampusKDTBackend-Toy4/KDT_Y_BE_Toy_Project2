package com.kdt_y_be_toy_project2.domain.trip.service;

import com.kdt_y_be_toy_project2.domain.trip.dto.*;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    @Transactional(readOnly = true)
    public List<GetTripResponse> getAllTrips() {
        return null;
    }

    @Transactional(readOnly = true)
    public GetTripResponse getTripById(final Long tripId) {
        return null;
    }

    public CreateTripResponse createTrip(final CreateTripRequest request) {
        return null;
    }

    public EditTripResponse editTrip(final Long tripId, final EditTripRequest request) {
        return null;
    }
}



