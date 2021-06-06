package com.approval.document.documentapproval.dto.document;

import com.approval.document.documentapproval.domain.entity.Approval;
import com.approval.document.documentapproval.domain.entity.DocumentType;
import com.approval.document.documentapproval.domain.entity.EasyDocument;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CreateDocumentRequestDto {

    @NonNull
    private String documentTitle;

    @NonNull
    private DocumentType documentType;

    @NonNull
    private String documentContent;

    @NonNull
    private List<ApprovalLineDto> approvalLine;

    @Builder
    public CreateDocumentRequestDto(String documentTitle,
                                    DocumentType documentType,
                                    String documentContent,
                                    List<ApprovalLineDto> approvalLine) {
        this.documentTitle = documentTitle;
        this.documentType = documentType;
        this.documentContent = documentContent;
        this.approvalLine = approvalLine;
    }

    public EasyDocument toEasyDocumentEntity() {
        return EasyDocument.builder()
            .title(this.documentTitle)
            .type(this.documentType)
            .content(this.documentContent)
            .ownerId(this.approvalLine.stream().sorted(
                    Comparator.comparing(ApprovalLineDto::getOrder))
                .findFirst()
                .get()
                .getUserId())
            .build();
    }

    public List<Approval> toApprovalList(Integer documentId) {
        return approvalLine.stream()
            .map(x -> x.toApprovalEntity(documentId))
            .collect(Collectors.toList());
    }
}
