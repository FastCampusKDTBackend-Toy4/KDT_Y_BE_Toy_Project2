package com.kdt_y_be_toy_project2.domain.trip.repository;

import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
}
