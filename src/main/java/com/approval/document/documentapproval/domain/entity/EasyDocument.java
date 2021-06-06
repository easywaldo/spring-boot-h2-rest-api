package com.approval.document.documentapproval.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "easy_document")
@NoArgsConstructor
public class EasyDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Integer documentId;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DocumentType type;

    @Column(name = "content")
    private String content;

    @Column(name = "owner_id")
    private String ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_status")
    private DocumentStatus documentStatus;

    @JsonIgnore
    @JoinColumn(name = "document_id")
    @OneToMany(fetch = FetchType.LAZY, targetEntity = Approval.class)
    private List<Approval> approvalList = new ArrayList<>();

    @Builder
    public EasyDocument(Integer documentId,
                        String title,
                        String content,
                        String ownerId,
                        DocumentType type,
                        DocumentStatus documentStatus) {
        this.documentId = documentId;
        this.title = title;
        this.content = content;
        this.ownerId = ownerId;
        this.type = type;
        this.documentStatus = documentStatus;
    }

    @JsonIgnore
    public Approval addApprovalUser(String userId) {
        int order = this.approvalList.size() + 1;
        return Approval.builder()
            .userId(userId)
            .order(order).build();
    }

    @JsonIgnore
    public void updateState(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }
}
