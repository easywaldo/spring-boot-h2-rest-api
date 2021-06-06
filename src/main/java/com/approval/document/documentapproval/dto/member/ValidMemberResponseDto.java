package com.approval.document.documentapproval.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ValidMemberResponseDto {
    private String validMemberMessage;
    @Builder
    public ValidMemberResponseDto(String validMemberMessage) {
        this.validMemberMessage = validMemberMessage;
    }
}
