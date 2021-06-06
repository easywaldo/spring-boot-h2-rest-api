package com.approval.document.documentapproval.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name ="document_approval")
@NoArgsConstructor
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_seq")
    private Integer approvalId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "is_approved")
    private boolean isApproved;

    @Column(name = "comment")
    private String comment;

    @Column(name = "approval_order")
    private int order;

    @Column(name = "document_id")
    private Integer documentId;

    @Builder
    public Approval(Integer documentId, String userId, int order) {
        this.documentId = documentId;
        this.userId = userId;
        this.order = order;
    }
}
