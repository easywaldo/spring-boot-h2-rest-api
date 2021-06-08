package com.approval.document.documentapproval.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ValidMemberResponseDto {
    private String validMemberMessage;
    private String userJwt;

    @Builder
    public ValidMemberResponseDto(
        String validMemberMessage,
        String userJwt) {
        this.validMemberMessage = validMemberMessage;
        this.userJwt = userJwt;
    }
}
