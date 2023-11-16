package com.kdt_y_be_toy_project2.global.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt_y_be_toy_project2.domain.itinerary.controller.ItineraryController;
import com.kdt_y_be_toy_project2.domain.trip.controller.TripController;
import com.kdt_y_be_toy_project2.global.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * This is for Controller layer UnitTest. If you don't want to check authentication and authorization
 * extend this class in your controller UnitTest class.
 */
@WebMvcTest(controllers = {ItineraryController.class, TripController.class},
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    })
public class ControllerUnitTestHelper {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
