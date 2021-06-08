package com.approval.document.documentapproval.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DocumentConfirmException extends RuntimeException {
    private String errorMessage;

    @Builder
    public DocumentConfirmException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
