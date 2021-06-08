package com.approval.document.documentapproval.dto.member;

import com.approval.document.documentapproval.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinMemberRequestDto {
    private String joinUserId;
    private String joinMemberName;
    private String joinUserPwd;

    @Builder
    public JoinMemberRequestDto(String joinUserId,
                                String joinMemberName,
                                String joinUserPwd) {
        this.joinUserId = joinUserId;
        this.joinMemberName = joinMemberName;
        this.joinUserPwd = joinUserPwd;
    }

    public Member toEntity() {
        return Member.builder()
            .memberName(this.getJoinMemberName())
            .userId(this.getJoinUserId())
            .userPwd(this.getJoinUserPwd())
            .build();
    }

}
