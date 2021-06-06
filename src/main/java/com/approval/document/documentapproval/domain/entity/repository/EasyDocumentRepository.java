package com.approval.document.documentapproval.domain.entity.repository;

import com.approval.document.documentapproval.domain.entity.EasyDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EasyDocumentRepository extends JpaRepository<EasyDocument, Integer> {
}
