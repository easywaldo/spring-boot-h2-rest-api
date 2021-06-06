package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.repository.ApprovalRepository;
import com.approval.document.documentapproval.domain.entity.repository.EasyDocumentRepository;
import com.approval.document.documentapproval.dto.document.CreateDocumentRequestDto;
import com.approval.document.documentapproval.dto.document.DocumentConfirmRequestDto;
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
        this.approvalRepository.saveAll(
            createDocumentRequestDto.toApprovalList(documentId));
        return documentId;
    }

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = false)
    public void confirmDocument(DocumentConfirmRequestDto requestDto) {
        var document = this.easyDocumentRepository.findById(requestDto.getDocumentId());
        var approval = document.get()
            .getApprovalList()
            .stream()
            .filter(x -> x.getApprovalId().equals(requestDto.getApprovalId()))
            .findFirst();
        if (approval.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 결재정보 입니다.");
        }
        if (!approval.get().getUserId().equals(requestDto.getUserId())) {
            throw new IllegalArgumentException("일치하지 않는 결재정보 입니다.");
        }
        approval.get().confirmDocument(requestDto);
    }
}
