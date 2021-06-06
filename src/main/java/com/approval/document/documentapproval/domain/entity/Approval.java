package com.approval.document.documentapproval.domain.entity;

import com.approval.document.documentapproval.dto.document.DocumentConfirmRequestDto;
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

    @Column(name = "is_confirm")
    private boolean isConfirm;

    @Builder
    public Approval(Integer documentId,
                    String userId,
                    boolean isApproved,
                    String comment,
                    int order,
                    boolean isConfirm) {
        this.documentId = documentId;
        this.userId = userId;
        this.isApproved = isApproved;
        this.comment = comment;
        this.order = order;
        this.isConfirm = isConfirm;
    }

    public void confirmDocument(DocumentConfirmRequestDto requestDto) {
        this.isApproved = requestDto.isApproved();
        this.isConfirm = true;
        this.comment = requestDto.getComment();
    }
}
