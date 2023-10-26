package com.kdt_y_be_toy_project2.domain.trip.service;

import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripRequest;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripResponse;
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
    public List<TripResponse> getAllTrips() {
        List<Trip> trips = tripRepository.findAll();

        if (trips.isEmpty()) {
            throw new TripNotFoundException();
        }
        return trips
                .stream().map(TripResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TripResponse getTripById(final Long tripId) {
        return tripRepository.findById(tripId)
                .map(TripResponse::from)
                .orElseThrow(TripNotFoundException::new);
    }

    public TripResponse createTrip(final TripRequest request) {
        request.timeScheduleInfo().isDateTimeValid();
        Trip newTrip = TripRequest.toEntity(request);

        return Optional.of(tripRepository.save(newTrip))
                .map(TripResponse::from)
                .orElseThrow();
    }

    public TripResponse editTrip(final Long tripId, final TripRequest request) {
        Trip updatedTrip = tripRepository.findById(tripId)
                .map(trip -> trip.update(TripRequest.toEntity(request)))
                .orElseThrow(TripNotFoundException::new);

        return Optional.of(tripRepository.save(updatedTrip))
                .map(TripResponse::from)
                .orElseThrow();
    }
}
