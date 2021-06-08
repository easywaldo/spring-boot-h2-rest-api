package com.approval.document.documentapproval.dto.document;

import com.approval.document.documentapproval.domain.entity.DocumentStatus;
import com.approval.document.documentapproval.domain.entity.DocumentType;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class DocumentAggregationDto {
    private Integer documentId;
    private String ownerId;
    private String title;
    private String content;
    private DocumentType type;
    private DocumentStatus documentStatus;
    @JsonAlias(value = "todoApproval")
    private List<ApprovalDto> todoApproval = new ArrayList<>();
    private List<ApprovalDto> allApprovalLine = new ArrayList<>();

    @Builder
    public DocumentAggregationDto(Integer documentId,
                                  String ownerId,
                                  String title,
                                  String content,
                                  DocumentType type,
                                  DocumentStatus documentStatus,
                                  List<ApprovalDto> todoApproval,
                                  List<ApprovalDto> allApprovalLine) {
        this.documentId = documentId;
        this.ownerId = ownerId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.documentStatus = documentStatus;
        this.todoApproval = todoApproval;
        this.allApprovalLine = allApprovalLine;
    }

    @Getter
    public static class ApprovalDto {
        private Integer approvalId;
        private String userId;
        private boolean isConfirm;
        private boolean isApproved;

        @Builder
        public ApprovalDto (Integer approvalId,
                            String userId,
                            boolean isConfirm,
                            boolean isApproved) {
            this.approvalId = approvalId;
            this.userId = userId;
            this.isConfirm = isConfirm;
            this.isApproved = isApproved;
        }
    }
}
