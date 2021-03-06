package com.approval.document.documentapproval.dto.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentQueryDto {

    private Integer documentId;

    private Integer approvalId;

    @JsonIgnore
    private boolean isPrevCheck;

    @JsonIgnore
    private boolean isLastCheck;

    @Builder
    public DocumentQueryDto(Integer documentId,
                            Integer approvedId,
                            boolean isPrevCheck,
                            boolean isLastCheck) {
        this.documentId = documentId;
        this.approvalId = approvedId;
        this.isPrevCheck = isPrevCheck;
        this.isLastCheck = isLastCheck;
    }
}
