package com.approval.document.documentapproval.dto.document;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchDocumentRequestDto {
    private String findUserId;

    @Builder
    public SearchDocumentRequestDto(String findUserId) {
        this.findUserId = findUserId;
    }
}
