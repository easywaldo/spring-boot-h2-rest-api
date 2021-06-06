package com.approval.document.documentapproval.dto.document;

import com.approval.document.documentapproval.domain.entity.Approval;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovalLineDto {
    private String userId;

    @Builder
    public ApprovalLineDto(String userId) {
        this.userId = userId;
    }

    public Approval toApprovalEntity(Integer documentId) {
        return Approval.builder()
            .userId(this.userId)
            .isApproved(false)
            .comment("")
            .documentId(documentId)
            .build();
    }
}
