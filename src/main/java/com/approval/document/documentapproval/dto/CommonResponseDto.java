package com.approval.document.documentapproval.dto;

import com.approval.document.documentapproval.domain.entity.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponseDto<T> {
    private String success;
    private String message;
    private T data;

    public CommonResponseDto(
        ResponseStatus success,
        String message,
        T data) {
        this.success = success.getCodeValue();
        this.message = message;
        this.data = data;
    }
}
