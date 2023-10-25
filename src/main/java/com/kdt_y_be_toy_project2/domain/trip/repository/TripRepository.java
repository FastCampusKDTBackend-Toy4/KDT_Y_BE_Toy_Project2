package com.kdt_y_be_toy_project2.domain.trip.repository;

import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import org.springframework.data.repository.CrudRepository;

public interface TripRepository extends CrudRepository<Long, Trip> {
}
