package com.approval.document.documentapproval.domain.entity;

public enum DocumentStatus {
    ING("ING"),
    NOT_APPROVED("NOT_APPROVED"),
    ALL_APPROVED("ALL_APPROVED");

    private String documentStatus;

    DocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }
}
