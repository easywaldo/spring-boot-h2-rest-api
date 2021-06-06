package com.approval.document.documentapproval.domain.entity;

public enum DocumentStatus {
    ING(0),
    NOT_APPROVED(1),
    ALL_APPROVED(2);

    private int documentStatus;

    DocumentStatus(int documentStatus) {
        this.documentStatus = documentStatus;
    }
}
