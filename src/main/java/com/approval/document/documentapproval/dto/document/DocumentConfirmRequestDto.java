package com.approval.document.documentapproval.dto.document;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class DocumentConfirmRequestDto {
    @NotNull
    private Integer approvalId;
    @NotNull
    private Integer documentId;
    @NotNull
    @NotEmpty
    private String userId;
    @NotNull
    private boolean approved;
    private String comment;

    @Builder
    public DocumentConfirmRequestDto(Integer approvalId,
                                     Integer documentId,
                                     String userId,
                                     String comment,
                                     boolean approved) {
        this.approvalId = approvalId;
        this.documentId = documentId;
        this.userId = userId;
        this.comment = comment;
        this.approved = approved;
    }
}
