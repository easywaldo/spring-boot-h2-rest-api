package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.DocumentStatus;
import com.approval.document.documentapproval.domain.entity.repository.ApprovalRepository;
import com.approval.document.documentapproval.domain.entity.repository.EasyDocumentRepository;
import com.approval.document.documentapproval.dto.document.CreateDocumentRequestDto;
import com.approval.document.documentapproval.dto.document.DocumentConfirmRequestDto;
import com.approval.document.documentapproval.dto.document.DocumentQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentService {
    private final EasyDocumentRepository easyDocumentRepository;
    private final ApprovalRepository approvalRepository;
    private final DocumentQueryGenerator documentQueryGenerator;

    @Autowired
    public DocumentService(EasyDocumentRepository easyDocumentRepository,
                           ApprovalRepository approvalRepository,
                           DocumentQueryGenerator documentQueryGenerator) {
        this.easyDocumentRepository = easyDocumentRepository;
        this.approvalRepository = approvalRepository;
        this.documentQueryGenerator = documentQueryGenerator;
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
        if (!document.get().getDocumentStatus().equals(DocumentStatus.ING)) {
            throw new IllegalArgumentException("결재가 종료된 결재정보 입니다.");
        }

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

        var unConfirmedList = documentQueryGenerator.selectDocumentViewModel(
            DocumentQueryDto.builder()
                .documentId(requestDto.getDocumentId())
                .approvedId(requestDto.getApprovalId())
                .build());
        if (!unConfirmedList.isEmpty()) {
            throw new IllegalArgumentException("확인되지 않은 이전단계의 결제라인이 존재합니다.");
        }

        var previousApproval = documentQueryGenerator.selectDocumentViewModel(
            DocumentQueryDto.builder()
                .documentId(requestDto.getDocumentId())
                .approvedId(requestDto.getApprovalId())
                .isPrevCheck(true)
                .build()
        );
        if (!previousApproval.isEmpty()) {
            throw new IllegalArgumentException("이미 반려가 된 내역이 존재합니다.");
        }

        var isLastApproval = documentQueryGenerator.selectDocumentViewModel(DocumentQueryDto
            .builder()
            .isLastCheck(true)
            .documentId(requestDto.getDocumentId())
            .approvedId(requestDto.getApprovalId())
            .build()).isEmpty();
        var approvalResult = approval.get();
        approvalResult.confirmDocument(requestDto);
        document.get().updateState(
            isLastApproval && requestDto.isApproved() ? DocumentStatus.ALL_APPROVED : requestDto.isApproved() ? DocumentStatus.ING : DocumentStatus.NOT_APPROVED);
        approvalRepository.save(approvalResult);
        easyDocumentRepository.save(document.get());
    }
}
