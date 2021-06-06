package com.approval.document.documentapproval.domain.entity;

public enum DocumentType {
    PRODUCT_PURCHASE("1"),
    EDUCATION_JOIN("2");

    private String documentType;

    DocumentType(String documentType) {
        this.documentType = documentType;
    }
}
