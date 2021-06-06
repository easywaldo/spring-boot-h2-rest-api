package com.approval.document.documentapproval.dto.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DocumentConfirmRequestDto {
    private Integer approvalId;
    private Integer documentId;
    private String userId;
    private boolean approved;
    private String comment;
    @JsonIgnore
    private boolean isLastApproval;

    @Builder
    public DocumentConfirmRequestDto(Integer approvalId,
                                     Integer documentId,
                                     String userId,
                                     String comment,
                                     boolean approved,
                                     boolean isLastApproval) {
        this.approvalId = approvalId;
        this.documentId = documentId;
        this.userId = userId;
        this.comment = comment;
        this.approved = approved;
        this.isLastApproval = isLastApproval;
    }
}
