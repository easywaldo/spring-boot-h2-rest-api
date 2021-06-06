package com.approval.document.documentapproval.domain.entity.repository;

import com.approval.document.documentapproval.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByUserId(String userId);
}
