package com.kdt_y_be_toy_project2.domain.itinerary.repository;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import com.kdt_y_be_toy_project2.domain.member.repository.MemberRepository;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.factory.ItineraryTestFactory;
import com.kdt_y_be_toy_project2.global.factory.MemberTestFactory;
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
    @Autowired
    private MemberRepository memberRepository;


    @Test
    void saveItinerary() {
        //given
        Member member = memberRepository.save(MemberTestFactory.createTestMemberWithRandomPassword());

        Trip returendTrip = tripRepository.save(TripTestFactory.createTestTrip(member));
        Itinerary itinerary = ItineraryTestFactory.createTestItinerary(returendTrip);


        //when
        Itinerary returnedItinerary= itineraryRepository.save(itinerary);

        //then
        assertEquals(itinerary, returnedItinerary);

    }

}