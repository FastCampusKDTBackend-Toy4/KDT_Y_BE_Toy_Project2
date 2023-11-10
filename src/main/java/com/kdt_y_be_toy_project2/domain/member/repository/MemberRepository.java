package com.kdt_y_be_toy_project2.domain.member.repository;

import com.kdt_y_be_toy_project2.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

}
