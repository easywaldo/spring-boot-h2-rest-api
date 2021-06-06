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

    @Column(name = "type")
    private DocumentType type;

    @JsonIgnore
    @JoinColumn(name = "document_id")
    @OneToMany(fetch = FetchType.LAZY, targetEntity = Approval.class)
    private List<Approval> approvalList = new ArrayList<>();

    @Builder
    public EasyDocument(Integer documentId,
                        String title,
                        DocumentType type) {
        this.documentId = documentId;
        this.title = title;
        this.type = type;
    }

    @JsonIgnore
    public Approval addApprovalUser(String userId) {
        int order = this.approvalList.size() + 1;
        return Approval.builder()
            .userId(userId)
            .order(order).build();
    }
}
