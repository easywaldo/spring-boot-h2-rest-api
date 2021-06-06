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
    private int order;

    @Builder
    public ApprovalLineDto(String userId, int order) {
        this.userId = userId;
        this.order = order;
    }

    public Approval toApprovalEntity(Integer documentId) {
        return Approval.builder()
            .userId(this.userId)
            .isApproved(false)
            .order(this.order)
            .comment("")
            .documentId(documentId)
            .build();
    }
}
