package com.approval.document.documentapproval.domain.entity.repository;

import com.approval.document.documentapproval.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByUserId(String userId);
    @Query(value = "select m from Member m where m.userId = ?1")
    Optional<Member> jpqlFindUserId(String userId);
}
