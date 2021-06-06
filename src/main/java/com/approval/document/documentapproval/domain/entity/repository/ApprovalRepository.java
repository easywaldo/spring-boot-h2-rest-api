package com.approval.document.documentapproval.domain.entity.repository;

import com.approval.document.documentapproval.domain.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, Integer> {
    List<Approval> findAllByDocumentId(Integer documentId);
}
