package com.approval.document.documentapproval.dto.document;

import com.approval.document.documentapproval.domain.entity.Approval;
import com.approval.document.documentapproval.domain.entity.DocumentStatus;
import com.approval.document.documentapproval.domain.entity.DocumentType;
import com.approval.document.documentapproval.domain.entity.EasyDocument;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CreateDocumentRequestDto implements Validator {

    @JsonIgnore
    private final SpringValidatorAdapter validator;

    @NotEmpty
    @NonNull
    private String documentTitle;

    private DocumentType documentType;

    @NotEmpty
    @NonNull
    private String documentContent;

    @Size(min = 1)
    @NotEmpty
    @NonNull
    private List<ApprovalLineDto> approvalLine;

    @Builder
    public CreateDocumentRequestDto(SpringValidatorAdapter validator,
                                    String documentTitle,
                                    DocumentType documentType,
                                    String documentContent,
                                    List<ApprovalLineDto> approvalLine) {
        this.validator = validator;
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
            .documentStatus(DocumentStatus.ING)
            .build();
    }

    public List<Approval> toApprovalList(Integer documentId) {
        return approvalLine.stream()
            .map(x -> x.toApprovalEntity(documentId))
            .collect(Collectors.toList());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return List.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(this.documentType.equals(DocumentType.EDUCATION_JOIN) && approvalLine.size() < 2) {
            errors.reject("교육신청서 작성오류", "최소 2명이상의 결재자가 필요합니다.");
        }
    }
}
