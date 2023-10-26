package com.kdt_y_be_toy_project2.domain.itinerary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.AccommodationInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.ItineraryRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.MoveInfoRequest;
import com.kdt_y_be_toy_project2.domain.itinerary.dto.StayInfoRequest;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;
import com.kdt_y_be_toy_project2.domain.trip.dto.TripRequest;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
class ItineraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final long tripId = 1;
    private final long itineraryId = 1;

    @Test
    void getAllItineraries() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/trips/" + tripId + "/itineraries")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getItineraryByTripId() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/trips/" + tripId + "/itineraries/" + itineraryId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createItinerary() throws Exception {

        ItineraryRequest itineraryRequest = itineraryRequest(2,1);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/trips/" + tripId + "/itineraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itineraryRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void editItinerary() throws  Exception{

        ItineraryRequest updateItineraryRequest = itineraryRequest(3,2);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/trips/" + tripId + "/itineraries/" + itineraryId)
                        .content(objectMapper.writeValueAsString(updateItineraryRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    private ItineraryRequest itineraryRequest(int startMinus, int endMinus) {
        return ItineraryRequest.builder()
                .accommodationInfoRequest(AccommodationInfoRequest.builder().startDateTime(LocalDateTime.now().minusDays(startMinus)).endDateTime(LocalDateTime.now().minusDays(endMinus)).build())
                .moveInfoRequest(MoveInfoRequest.builder().startDateTime(LocalDateTime.now().minusDays(startMinus)).endDateTime(LocalDateTime.now().minusDays(endMinus)).build())
                .stayInfoRequest(StayInfoRequest.builder().startDateTime(LocalDateTime.now().minusDays(startMinus)).endDateTime(LocalDateTime.now().minusDays(endMinus)).build())
                .build();

    }

}