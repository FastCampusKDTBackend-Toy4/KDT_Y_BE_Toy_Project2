package com.kdt_y_be_toy_project2.domain.itinerary.service;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryDTO;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.TripNotFoundException;
import com.kdt_y_be_toy_project2.domain.itinerary.repository.ItineraryRepository;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TripRepository tripRepository;

    @Transactional(readOnly = true)
    public List<ItineraryDTO> getAllItineraries(final Long tripId) {
        return tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new)
                .getItineraries().stream().map(itinerary -> ItineraryDTO.from(itinerary)).toList();
    }

    @Transactional(readOnly = true)
    public ItineraryDTO getItineraryById(final Long tripId, final Long itineraryId) {
        Trip retrivedTrip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);

        for (Itinerary itinerary : retrivedTrip.getItineraries()) {
            if (itinerary.getId() == itineraryId) {
                return ItineraryDTO
                        .from(itineraryRepository
                                .findById(itineraryId)
                                .orElseThrow(() -> new RuntimeException("여정 정보를 찾을 수 없습니다.")));
            }
        }

        throw new RuntimeException("여행 id 와 여정 id관련 여정 entity가 없습니다.");
    }

    public ItineraryDTO createItinerary(final Long tripId, final ItineraryDTO request) {
        Trip retrivedTrip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
        Itinerary savedItinerary = itineraryRepository.save(Itinerary.to(request, retrivedTrip));
        retrivedTrip.getItineraries().add(savedItinerary);

        return null;
    }

    public ItineraryDTO editItinerary(final Long itinerary_id, final ItineraryDTO request) {
        Itinerary updatedItinerary = itineraryRepository.findById(itinerary_id)
                .map(itinerary -> itinerary.update(Itinerary.to(request, itinerary.getTrip())))
                .orElseThrow(() -> new RuntimeException("여정 정보를 찾을 수 없습니다."));
        return Optional.of(itineraryRepository.save(updatedItinerary))
                .map(ItineraryDTO::from)
                .orElseThrow();
    }
}