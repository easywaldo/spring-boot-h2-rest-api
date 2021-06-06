package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.Approval;
import com.approval.document.documentapproval.domain.entity.DocumentType;
import com.approval.document.documentapproval.domain.entity.repository.ApprovalRepository;
import com.approval.document.documentapproval.dto.document.ApprovalLineDto;
import com.approval.document.documentapproval.dto.document.CreateDocumentRequestDto;
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
    private ApprovalRepository approvalRepository;

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
}
