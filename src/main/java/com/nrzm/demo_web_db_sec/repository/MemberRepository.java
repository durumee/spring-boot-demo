package com.nrzm.demo_web_db_sec.repository;

import com.nrzm.demo_web_db_sec.entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberName(String memberName);
}
