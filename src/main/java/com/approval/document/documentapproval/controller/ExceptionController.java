package com.approval.document.documentapproval.controller;

import com.approval.document.documentapproval.domain.entity.DocumentConfirmException;
import com.approval.document.documentapproval.domain.entity.ResponseStatus;
import com.approval.document.documentapproval.dto.CommonResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler({IllegalAccessException.class, RuntimeException.class, Exception.class,})
    @ResponseBody
    public ResponseEntity<CommonResponseDto<?>> BadRequestException(final Exception ex) {

        if (ex.getClass().toString().equals("class java.lang.IllegalAccessException")) {
            return ResponseEntity.badRequest().body(
                new CommonResponseDto<>(
                    ResponseStatus.RES_CODE_SERVICE,
                    "invalid jwt token value",
                    null));
        }
        else {
            return ResponseEntity.badRequest().body(
                new CommonResponseDto<>(ResponseStatus.RES_CODE_SERVICE, "", null));
        }
    }

    @ExceptionHandler({DocumentConfirmException.class})
    @ResponseBody
    public ResponseEntity<CommonResponseDto<?>> documentConfirmExceptionHandler(final DocumentConfirmException ex) {
        return ResponseEntity.badRequest().body(
            new CommonResponseDto<>(
                ResponseStatus.RES_CODE_SERVICE,
                ex.getErrorMessage(),
                null));
    }
}
