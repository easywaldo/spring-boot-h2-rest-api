package com.approval.document.documentapproval.dto.document;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class DocumentPagingRequestDto {
    @NotNull
    @NotEmpty
    private String userId;
    @Min(value = 0)
    private int offset;
    @Min(value = 10)
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
