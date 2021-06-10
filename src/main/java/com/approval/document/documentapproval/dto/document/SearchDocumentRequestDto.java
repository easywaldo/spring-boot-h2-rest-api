package com.approval.document.documentapproval.dto.document;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class SearchDocumentRequestDto {
    @NotNull
    @NotEmpty
    private String findUserId;

    @Builder
    public SearchDocumentRequestDto(String findUserId) {
        this.findUserId = findUserId;
    }
}
