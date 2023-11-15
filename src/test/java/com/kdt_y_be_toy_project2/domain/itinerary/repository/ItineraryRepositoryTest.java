package com.kdt_y_be_toy_project2.domain.itinerary.repository;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.factory.ItineraryTestFactory;
import com.kdt_y_be_toy_project2.global.factory.TripTestFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class ItineraryRepositoryTest {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ItineraryRepository itineraryRepository;


    @Test
    void saveItinerary() {
        //given
        Trip returendTrip = tripRepository.save(TripTestFactory.createTestTrip());
        Itinerary itinerary = ItineraryTestFactory.createTestItinerary(returendTrip);
        //when
        Itinerary returnedItinerary= itineraryRepository.save(itinerary);

        //then
        assertEquals(itinerary, returnedItinerary);

    }

}