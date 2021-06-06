package com.approval.document.documentapproval.domain.entity;

public enum DocumentType {
    PRODUCT_PURCHASE("PRODUCT_PURCHASE"),
    EDUCATION_JOIN("EDUCATION_JOIN");

    private String documentType;

    DocumentType(String documentType) {
        this.documentType = documentType;
    }
}
