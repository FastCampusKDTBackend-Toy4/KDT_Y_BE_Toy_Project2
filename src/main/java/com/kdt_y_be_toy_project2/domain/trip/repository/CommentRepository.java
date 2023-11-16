package com.kdt_y_be_toy_project2.domain.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdt_y_be_toy_project2.domain.trip.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
