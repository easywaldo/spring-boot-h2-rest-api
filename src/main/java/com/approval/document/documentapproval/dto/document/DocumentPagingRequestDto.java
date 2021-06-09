package com.approval.document.documentapproval.dto.document;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DocumentPagingRequestDto {
    private String userId;
    private int offset;
    private int limit;

    @Builder
    public DocumentPagingRequestDto(String userId,
                                    int offset,
                                    int limit) {
        this.userId = userId;
        this.offset = offset;
        this.limit = limit;
    }
}
