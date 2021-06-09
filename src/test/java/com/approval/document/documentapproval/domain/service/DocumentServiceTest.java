package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.Approval;
import com.approval.document.documentapproval.domain.entity.DocumentConfirmException;
import com.approval.document.documentapproval.domain.entity.DocumentType;
import com.approval.document.documentapproval.domain.entity.repository.ApprovalRepository;
import com.approval.document.documentapproval.domain.entity.repository.EasyDocumentRepository;
import com.approval.document.documentapproval.dto.document.ApprovalLineDto;
import com.approval.document.documentapproval.dto.document.CreateDocumentRequestDto;
import com.approval.document.documentapproval.dto.document.DocumentConfirmRequestDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private EasyDocumentRepository easyDocumentRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Before
    public void initData() {
        approvalRepository.deleteAll();
        easyDocumentRepository.deleteAll();
    }

    @Test
    public void given_document_request_then_create_document_should_return_document_id() {
        // arrange
        List<ApprovalLineDto> approvalLine = new ArrayList<>();
        approvalLine.add(ApprovalLineDto.builder()
            .userId("easywaldo").order(1).build());
        approvalLine.add(ApprovalLineDto.builder()
            .userId("myboss").order(2).build());

        CreateDocumentRequestDto createDocumentRequestDto = CreateDocumentRequestDto.builder()
            .documentTitle("교육신청서")
            .documentType(DocumentType.EDUCATION_JOIN)
            .documentContent("Event Driven Architecture And Domain Driven")
            .approvalLine(approvalLine)
            .build();

        // act
        var documentId = documentService.createDocument(createDocumentRequestDto);

        // assert
        List<Approval> appovalResult = approvalRepository.findAllByDocumentId(documentId);
        List<String> approvalUsers = appovalResult.stream().map(Approval::getUserId).collect(Collectors.toList());
        assertThat(approvalLine.stream().filter(x -> approvalUsers.contains(x.getUserId())).count()).isEqualTo(2L);
    }

    @Test(expected = DocumentConfirmException.class)
    public void given_document_approval_confirm_request_with_un_confirmed_prev_user_then_confirm_service_should_throws_exception() {
        // arrange
        List<ApprovalLineDto> approvalLine = new ArrayList<>();
        approvalLine.add(ApprovalLineDto.builder()
            .userId("easywaldo").order(1).build());
        approvalLine.add(ApprovalLineDto.builder()
            .userId("myboss").order(2).build());

        CreateDocumentRequestDto createDocumentRequestDto = CreateDocumentRequestDto.builder()
            .documentTitle("교육신청서")
            .documentType(DocumentType.EDUCATION_JOIN)
            .documentContent("Event Driven Architecture And Domain Driven")
            .approvalLine(approvalLine)
            .build();

        // act
        var documentId = documentService.createDocument(createDocumentRequestDto);
        List<Approval> approvalList = approvalRepository.findAllByDocumentId(documentId);
        Integer approvalId = approvalList.stream().filter(x -> x.getUserId().equals("myboss")).findFirst().get().getApprovalId();
        DocumentConfirmRequestDto requestDto = DocumentConfirmRequestDto
            .builder()
            .approvalId(approvalId)
            .documentId(documentId)
            .approved(true)
            .userId("myboss")
            .build();
        documentService.confirmDocument(requestDto);

        // assert
    }

    @Test
    public void given_document_approval_confirm_request_with_all_confirmed_prev_user_then_confirm_service_should_update_correctly() {
        // arrange
        List<ApprovalLineDto> approvalLine = new ArrayList<>();
        approvalLine.add(ApprovalLineDto.builder()
            .userId("easywaldo").order(1).build());
        approvalLine.add(ApprovalLineDto.builder()
            .userId("myboss").order(2).build());

        CreateDocumentRequestDto createDocumentRequestDto = CreateDocumentRequestDto.builder()
            .documentTitle("교육신청서")
            .documentType(DocumentType.EDUCATION_JOIN)
            .documentContent("Event Driven Architecture And Domain Driven")
            .approvalLine(approvalLine)
            .build();

        // act
        var documentId = documentService.createDocument(createDocumentRequestDto);
        List<Approval> approvalList = approvalRepository.findAllByDocumentId(documentId);
        Integer firstApproval = approvalList.stream().filter(x -> x.getUserId().equals("easywaldo")).findFirst().get().getApprovalId();
        DocumentConfirmRequestDto requestDto = DocumentConfirmRequestDto
            .builder()
            .approvalId(firstApproval)
            .documentId(documentId)
            .approved(true)
            .userId("easywaldo")
            .build();
        documentService.confirmDocument(requestDto);

        Integer secondApproval = approvalList.stream().filter(x -> x.getUserId().equals("myboss")).findFirst().get().getApprovalId();
        requestDto = DocumentConfirmRequestDto
            .builder()
            .approvalId(secondApproval)
            .documentId(documentId)
            .approved(true)
            .userId("myboss")
            .build();
        documentService.confirmDocument(requestDto);

        // assert
        assertThat(approvalRepository.findById(firstApproval).get().isApproved())
            .isEqualTo(true);
        assertThat(approvalRepository.findById(firstApproval).get().isConfirm())
            .isEqualTo(true);
    }
}
