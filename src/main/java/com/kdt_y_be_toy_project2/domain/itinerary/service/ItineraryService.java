package com.kdt_y_be_toy_project2.domain.itinerary.service;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryResponse;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.InvalidDateException;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.InvalidItineraryDurationException;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.ItineraryNotFoundException;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.TripNotFoundException;
import com.kdt_y_be_toy_project2.domain.itinerary.repository.ItineraryRepository;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.util.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TripRepository tripRepository;

    @Transactional(readOnly = true)
    public List<ItineraryResponse> getAllItineraries(final Long tripId) {

        List<ItineraryResponse> itineraryResponses = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new)
                .getItineraries().stream().map(itinerary -> ItineraryResponse.from(itinerary)).toList();

        if (itineraryResponses.size() == 0) {
            throw new ItineraryNotFoundException();
        }
        return itineraryResponses;
    }

    @Transactional(readOnly = true)
    public ItineraryResponse getItineraryById(final Long tripId, final Long itineraryId) {
        Trip retrivedTrip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);

        for (Itinerary itinerary : retrivedTrip.getItineraries()) {
            if (itinerary.getId() == itineraryId) {
                return ItineraryResponse
                        .from(itineraryRepository
                                .findById(itineraryId)
                                .orElseThrow(() -> new ItineraryNotFoundException()));
            }
        }
        throw new ItineraryNotFoundException();
    }

    public ItineraryResponse createItinerary(final Long tripId, final ItineraryRequest request) {
        Trip retrivedTrip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
        checkItineraryDuration(retrivedTrip, request);
        checkInvalidDate(request);
        Itinerary savedItinerary = itineraryRepository.save(ItineraryRequest.toEntity(request, retrivedTrip));
        retrivedTrip.getItineraries().add(savedItinerary);

        return ItineraryResponse.from(savedItinerary);
    }

    public ItineraryResponse editItinerary(final Long tripId, final Long itineraryId, final ItineraryRequest request) {

        Trip retrivedTrip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);

        Itinerary updatedItinerary = itineraryRepository.findById(itineraryId)
                .map(itinerary -> itinerary.update(ItineraryRequest.toEntity(request, itinerary.getTrip())))
                .orElseThrow(() -> new ItineraryNotFoundException());

        checkItineraryDuration(updatedItinerary.getTrip(), request);
        checkInvalidDate(request);

        return Optional.of(itineraryRepository.save(updatedItinerary))
                .map(ItineraryResponse::from)
                .orElseThrow();
    }

    void checkItineraryDuration(Trip trip, ItineraryRequest itinerary){
        LocalDateTime tripStartTime = trip.getTripSchedule().getStartDate().atStartOfDay();
        LocalDateTime tripEndTime = trip.getTripSchedule().getEndDate().atStartOfDay().plusDays(1);

        if(LocalDateTimeUtil.toLocalDateTime(itinerary.stayInfoRequest().startDateTime()).isBefore(tripStartTime) ||
                LocalDateTimeUtil.toLocalDateTime(itinerary.accommodationInfoRequest().startDateTime()).isBefore(tripStartTime) ||
                LocalDateTimeUtil.toLocalDateTime(itinerary.moveInfoRequest().startDateTime()).isBefore(tripStartTime) ||
                LocalDateTimeUtil.toLocalDateTime(itinerary.stayInfoRequest().endDateTime()).isAfter(tripEndTime) ||
                LocalDateTimeUtil.toLocalDateTime(itinerary.accommodationInfoRequest().endDateTime()).isAfter(tripEndTime) ||
                LocalDateTimeUtil.toLocalDateTime(itinerary.moveInfoRequest().endDateTime()).isAfter(tripEndTime)
        ) throw new InvalidItineraryDurationException();
    }

    void checkInvalidDate(ItineraryRequest itinerary){

        if(     LocalDateTimeUtil.toLocalDateTime(itinerary.moveInfoRequest().startDateTime()).isAfter(LocalDateTimeUtil.toLocalDateTime(itinerary.moveInfoRequest().endDateTime())) ||
                LocalDateTimeUtil.toLocalDateTime(itinerary.accommodationInfoRequest().startDateTime()).isAfter(LocalDateTimeUtil.toLocalDateTime(itinerary.accommodationInfoRequest().endDateTime())) ||
                LocalDateTimeUtil.toLocalDateTime(itinerary.stayInfoRequest().startDateTime()).isAfter(LocalDateTimeUtil.toLocalDateTime(itinerary.stayInfoRequest().endDateTime()))
        ) throw new InvalidDateException();
    }
}
