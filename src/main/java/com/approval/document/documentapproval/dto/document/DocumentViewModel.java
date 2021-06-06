package com.approval.document.documentapproval.dto.document;

import com.approval.document.documentapproval.domain.entity.DocumentStatus;
import com.approval.document.documentapproval.domain.entity.DocumentType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentViewModel {
    private Integer documentId;
    private String title;
    private String content;
    private DocumentType type;
    private DocumentStatus documentStatus;
    private String ownerId;
    private Integer approvalId;
    private String userId;
    private boolean isConfirm;
    private boolean isApproved;

    @Builder
    public DocumentViewModel(Integer documentId,
                             String ownerId,
                             String title,
                             String content,
                             DocumentType type,
                             DocumentStatus documentStatus,
                             Integer approvalId,
                             String userId,
                             boolean isConfirm,
                             boolean isApproved) {
        this.documentId = documentId;
        this.ownerId = ownerId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.documentStatus = documentStatus;
        this.approvalId = approvalId;
        this.userId = userId;
        this.isConfirm = isConfirm;
        this.isApproved = isApproved;
    }
}
