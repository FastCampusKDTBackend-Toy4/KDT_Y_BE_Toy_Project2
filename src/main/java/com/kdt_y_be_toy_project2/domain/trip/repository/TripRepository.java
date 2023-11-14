package com.kdt_y_be_toy_project2.domain.trip.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.TripType;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

	@Query("SELECT t FROM Trip t WHERE " +
		"(:trip_name is null or t.name LIKE %:trip_name%) and " +
		"(:trip_type is null or t.tripType = :trip_type) and " +
		"(:start_date is null or t.tripSchedule.startDate >= :start_date) and " +
		"(:end_date is null or t.tripSchedule.endDate <= :end_date)")
	List<Trip> searchTrips(
		@Param("trip_name") String tripName,
		@Param("trip_type") TripType tripType,
		@Param("start_date") LocalDate startDate,
		@Param("end_date") LocalDate endDate
	);
}
