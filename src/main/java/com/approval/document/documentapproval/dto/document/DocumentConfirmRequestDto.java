package com.approval.document.documentapproval.dto.document;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DocumentConfirmRequestDto {
    private Integer approvalId;
    private Integer documentId;
    private String userId;
    private boolean isApproved;

    @Builder
    public DocumentConfirmRequestDto(Integer approvalId,
                                     Integer documentId,
                                     String userId,
                                     boolean isApproved) {
        this.approvalId = approvalId;
        this.documentId = documentId;
        this.userId = userId;
        this.isApproved = isApproved;
    }
}
