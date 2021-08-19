package com.approval.document.documentapproval.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Table(name="member_bulk")
@NoArgsConstructor
public class MemberForBulk implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "member_seq")
    private Integer memberSeq;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pwd")
    private String userPwd;

    @Builder
    public MemberForBulk(Integer memberSeq,
                  String memberName,
                  String userId,
                  String userPwd) {
        this.memberSeq = memberSeq;
        this.memberName = memberName;
        this.userId = userId;
        this.userPwd = userPwd;
    }
}
