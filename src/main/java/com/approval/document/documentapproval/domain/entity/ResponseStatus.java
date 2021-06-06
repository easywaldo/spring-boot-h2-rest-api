package com.approval.document.documentapproval.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum ResponseStatus {
    RES_CODE_SUCCESS("0000"),
    RES_CODE_SERVICE("6000");

    @JsonIgnore
    private final String codeValue;

    @JsonIgnore
    ResponseStatus(String s) {
        this.codeValue = s;
    }

    @JsonIgnore
    public String getCodeValue() {
        return this.codeValue;
    }
}
