package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.DocumentConfirmException;
import com.approval.document.documentapproval.domain.entity.DocumentStatus;
import com.approval.document.documentapproval.domain.entity.repository.ApprovalRepository;
import com.approval.document.documentapproval.domain.entity.repository.EasyDocumentRepository;
import com.approval.document.documentapproval.dto.document.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        var documentId = this.easyDocumentRepository.saveAndFlush(
            createDocumentRequestDto.toEasyDocumentEntity()).getDocumentId();
        this.approvalRepository.saveAll(
            createDocumentRequestDto.toApprovalList(documentId));
        return documentId;
    }

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = false)
    public void confirmDocument(DocumentConfirmRequestDto requestDto) {
        var document = this.easyDocumentRepository.findById(requestDto.getDocumentId());
        if (document.isEmpty()) {
            throw new DocumentConfirmException("존재하지 않는 결재정보 입니다.");
        }
        if (!document.get().getDocumentStatus().equals(DocumentStatus.ING)) {
            throw new DocumentConfirmException("결재가 종료된 결재정보 입니다.");
        }

        var approval = document.get()
            .getApprovalList()
            .stream()
            .filter(x -> x.getApprovalId().equals(requestDto.getApprovalId()))
            .findFirst();
        if (approval.isEmpty()) {
            throw new DocumentConfirmException("존재하지 않는 결재정보 입니다.");
        }
        if (!approval.get().getUserId().equals(requestDto.getUserId())) {
            throw new DocumentConfirmException("일치하지 않는 결재정보 입니다.");
        }

        var unConfirmedList = documentQueryGenerator.selectDocumentViewModel(
            DocumentQueryDto.builder()
                .documentId(requestDto.getDocumentId())
                .approvedId(requestDto.getApprovalId())
                .build());
        if (!unConfirmedList.isEmpty()) {
            throw new DocumentConfirmException("확인되지 않은 이전단계의 결제라인이 존재합니다.");
        }

        var previousApproval = documentQueryGenerator.selectDocumentViewModel(
            DocumentQueryDto.builder()
                .documentId(requestDto.getDocumentId())
                .approvedId(requestDto.getApprovalId())
                .isPrevCheck(true)
                .build()
        );
        if (!previousApproval.isEmpty()) {
            throw new DocumentConfirmException("이미 반려가 된 내역이 존재합니다.");
        }

        var isLastApproval = documentQueryGenerator.selectDocumentViewModel(
            DocumentQueryDto
                .builder()
                .isLastCheck(true)
                .documentId(requestDto.getDocumentId())
                .approvedId(requestDto.getApprovalId())
                .build()
        ).isEmpty();
        var approvalResult = approval.get();
        approvalResult.confirmDocument(requestDto);
        document.get().updateState(
            isLastApproval && requestDto.isApproved() ? DocumentStatus.ALL_APPROVED : requestDto.isApproved() ? DocumentStatus.ING : DocumentStatus.NOT_APPROVED);
        approvalRepository.save(approvalResult);
        easyDocumentRepository.save(document.get());
    }

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = true)
    public List<DocumentAggregationDto> selectOutBox(SearchDocumentRequestDto owner) {
        return documentQueryGenerator.selectOutBox(owner.getFindUserId());
    }

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = true)
    public List<DocumentAggregationDto> selectInBox(SearchDocumentRequestDto approveUser) {
        return documentQueryGenerator.selectInBox(approveUser.getFindUserId());
    }

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = true)
    public List<DocumentAggregationDto> selectArchive(DocumentPagingRequestDto requestDto) {
        return documentQueryGenerator.selectArchive(requestDto);
    }
}
