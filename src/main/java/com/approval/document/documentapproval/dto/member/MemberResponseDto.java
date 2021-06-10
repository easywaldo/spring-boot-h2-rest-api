package com.approval.document.documentapproval.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private String userId;
    private String memberName;

    @Builder
    public MemberResponseDto(String userId,
                             String memberName) {
        this.userId = userId;
        this.memberName = memberName;
    }
}
