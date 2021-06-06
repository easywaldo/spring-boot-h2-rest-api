package com.approval.document.documentapproval.controller;

import com.approval.document.documentapproval.domain.entity.ResponseStatus;
import com.approval.document.documentapproval.domain.service.DocumentService;
import com.approval.document.documentapproval.dto.CommonResponseDto;
import com.approval.document.documentapproval.dto.document.CreateDocumentRequestDto;
import com.approval.document.documentapproval.dto.document.DocumentConfirmRequestDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/document")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @ApiOperation(value = "전자결재문서 생성", notes = "전자결재문서를 생성한다")
    @PostMapping("/create")
    public CommonResponseDto<Integer> createDocument(
        @RequestBody CreateDocumentRequestDto requestDto) {

        var documentId = this.documentService.createDocument(requestDto);
        return new CommonResponseDto<>(
            ResponseStatus.RES_CODE_SUCCESS, "success", documentId);
    }

    @ApiOperation(value = "결재문서확인", notes = "전자결재문서를 확인한다")
    @PostMapping("/confirm")
    public CommonResponseDto confirmDocument(
        @RequestBody DocumentConfirmRequestDto requestDto) {

        documentService.confirmDocument(requestDto);
        return new CommonResponseDto<>(
            ResponseStatus.RES_CODE_SUCCESS, "success", null);
    }

}
