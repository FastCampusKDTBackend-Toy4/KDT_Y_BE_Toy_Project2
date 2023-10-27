package com.kdt_y_be_toy_project2.domain.itinerary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.repository.ItineraryRepository;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.repository.TripRepository;
import com.kdt_y_be_toy_project2.global.factory.ItineraryTestFactory;
import com.kdt_y_be_toy_project2.global.factory.TripTestFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ItineraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItineraryRepository itineraryRepository;
    @Autowired
    private TripRepository tripRepository;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GetAll{

        private Trip savedTrip;
        private Itinerary savedItinerary;

        @BeforeAll
        void beforeAll() {
            // given
            savedTrip = tripRepository.save(TripTestFactory.createTestTrip());
            savedItinerary = itineraryRepository.save(ItineraryRequest.toEntity(ItineraryTestFactory.createTestItineraryRequest(),savedTrip));
            savedItinerary = itineraryRepository.save(ItineraryRequest.toEntity(ItineraryTestFactory.createTestItineraryRequest(),savedTrip));

        }

        @Test
        void getAllItineraries() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get("/v1/trips/" + savedTrip.getId() + "/itineraries")
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        }

    }


    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Get {

        private Trip savedTrip;
        private Itinerary savedItinerary;

        @BeforeAll
        void beforeAll() {
            // given
            savedTrip = tripRepository.save(TripTestFactory.createTestTrip());
            savedItinerary = itineraryRepository.save(ItineraryRequest.toEntity(ItineraryTestFactory.createTestItineraryRequest(),savedTrip));

        }
        @Test
        void getItineraryByTripId() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get("/v1/trips/" + savedTrip.getId() + "/itineraries/" + savedItinerary.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Creat{
        private Trip savedTrip;

        @BeforeAll
        void beforeAll() {
            // given
            savedTrip = tripRepository.save(TripTestFactory.createTestTrip());
        }

        @Test
        void createItinerary() throws Exception {

            ItineraryRequest itineraryRequest = ItineraryTestFactory.createSuccessTestItineraryRequest();

            mockMvc.perform(MockMvcRequestBuilders.post("/v1/trips/" + savedTrip.getId() + "/itineraries")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(itineraryRequest)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Edit {

        private Trip savedTrip;
        private Itinerary savedItinerary;

        @BeforeAll
        void beforeAll() {
            // given
            savedTrip = tripRepository.save(TripTestFactory.createTestTrip());
            savedItinerary = itineraryRepository.save(ItineraryRequest.toEntity(ItineraryTestFactory.createTestItineraryRequest(),savedTrip));

        }


        @Test
        void editItinerary() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.put("/v1/trips/" + savedTrip.getId() + "/itineraries/" + savedItinerary.getId())
                            .content(objectMapper.writeValueAsString(ItineraryTestFactory.createTestItineraryRequest()))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        }
    }


}