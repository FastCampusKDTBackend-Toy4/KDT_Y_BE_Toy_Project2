package com.kdt_y_be_toy_project2.domain.itinerary.repository;

import com.kdt_y_be_toy_project2.domain.itinerary.domain.Itinerary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface ItineraryRepository extends CrudRepository<Itinerary, Long> {

}
