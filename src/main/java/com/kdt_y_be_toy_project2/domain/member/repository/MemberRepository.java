package com.kdt_y_be_toy_project2.domain.member.repository;

import java.util.Optional;

import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

	Optional<Member> findByEmail(String email);
}
