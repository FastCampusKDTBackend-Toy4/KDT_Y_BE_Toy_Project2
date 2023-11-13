package com.kdt_y_be_toy_project2.domain.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kdt_y_be_toy_project2.domain.trip.domain.Likes;
import com.kdt_y_be_toy_project2.domain.trip.domain.Trip;
import com.kdt_y_be_toy_project2.domain.trip.domain.id.LikesID;

@Repository
public interface LikesRepository extends JpaRepository<Likes, LikesID> {
	Long countByTripId(Long tripId);

	@Query("select l.trip from Likes l where l.member.email = :email")
	List<Trip> getAllTripByMemberEmail(@Param("email") String memberEmail);
}
