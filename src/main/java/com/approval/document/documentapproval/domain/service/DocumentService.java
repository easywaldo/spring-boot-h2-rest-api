package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.repository.ApprovalRepository;
import com.approval.document.documentapproval.domain.entity.repository.EasyDocumentRepository;
import com.approval.document.documentapproval.dto.document.CreateDocumentRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentService {
    private final EasyDocumentRepository easyDocumentRepository;
    private final ApprovalRepository approvalRepository;

    @Autowired
    public DocumentService(EasyDocumentRepository easyDocumentRepository,
                           ApprovalRepository approvalRepository) {
        this.easyDocumentRepository = easyDocumentRepository;
        this.approvalRepository = approvalRepository;
    }

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = false)
    public Integer createDocument(CreateDocumentRequestDto createDocumentRequestDto) {
        var documentId = this.easyDocumentRepository.save(
            createDocumentRequestDto.toEasyDocumentEntity()).getDocumentId();
        this.approvalRepository.saveAll(createDocumentRequestDto.toApprovalList(documentId));
        return documentId;
    }
}
