package com.approval.document.documentapproval.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_seq")
    private Integer memberSeq;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pwd")
    private String userPwd;

    @Transient
    private String roleGroup;
    public String getRoleGroup() {
        return "general";
    }

    @Builder
    public Member(Integer memberSeq,
                  String memberName,
                  String userId,
                  String userPwd) {
        this.memberSeq = memberSeq;
        this.memberName = memberName;
        this.userId = userId;
        this.userPwd = userPwd;
    }

    public void update(String memberName) {
        this.memberName = memberName;
    }
}
