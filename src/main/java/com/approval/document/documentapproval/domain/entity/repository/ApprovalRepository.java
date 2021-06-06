package com.approval.document.documentapproval.domain.entity.repository;

import com.approval.document.documentapproval.domain.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRepository extends JpaRepository<Approval, Integer> {
}
