package com.approval.document.documentapproval.controller;

import com.approval.document.documentapproval.config.ViolationsMessageGenerator;
import com.approval.document.documentapproval.domain.entity.ResponseStatus;
import com.approval.document.documentapproval.domain.service.AuthService;
import com.approval.document.documentapproval.domain.service.CustomValidator;
import com.approval.document.documentapproval.domain.service.DocumentService;
import com.approval.document.documentapproval.dto.CommonResponseDto;
import com.approval.document.documentapproval.dto.document.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/document")
public class DocumentController {

    private final AuthService authService;
    private final DocumentService documentService;
    private final CustomValidator customValidator;
    private final Validator validator;

    @Autowired
    public DocumentController(DocumentService documentService,
                              CustomValidator customValidator,
                              AuthService authService,
                              Validator validator) {
        this.documentService = documentService;
        this.customValidator = customValidator;
        this.authService = authService;
        this.validator = validator;
    }

    @ApiOperation(value = "전자결재문서 생성", notes = "전자결재문서를 생성한다")
    @PostMapping("/create")
    public CommonResponseDto<?> createDocument(
        @RequestBody CreateDocumentRequestDto requestDto,
        HttpServletRequest request,
        @ApiIgnore Errors errors) {

        String ownerId = requestDto.getApprovalLine().stream().min(
            Comparator.comparing(ApprovalLineDto::getOrder)).get().getUserId();
        if (!ownerId.equals(authService.getUserIdFromJwtCookie(request))) {
            return new CommonResponseDto(
                ResponseStatus.RES_CODE_SERVICE,
                "fail",
                "일치하지 않는 인증정보 입니다.");
        }

        List<ConstraintViolation<CreateDocumentRequestDto>> violations =
            this.validator.validate(requestDto).stream().collect(Collectors.toList());
        if (violations.stream().count() > 0) {
            return new CommonResponseDto(
                ResponseStatus.RES_CODE_SERVICE,
                "fail",
                new ViolationsMessageGenerator().getMessage(violations)
            );
        }

        requestDto.validate(requestDto, errors);
        if (errors.hasErrors()) {
            return new CommonResponseDto(
                ResponseStatus.RES_CODE_SERVICE,
                "fail",
                errors.getAllErrors());
        }

        this.customValidator.validate(requestDto.getApprovalLine(), errors);
        if (errors.hasErrors()) {
            return new CommonResponseDto(
                ResponseStatus.RES_CODE_SERVICE,
                "fail",
                errors.getAllErrors());
        }

        var documentId = this.documentService.createDocument(requestDto);
        return new CommonResponseDto<>(
            ResponseStatus.RES_CODE_SUCCESS, "success", documentId);
    }

    @ApiOperation(value = "결재문서확인", notes = "전자결재문서를 확인한다")
    @PostMapping("/confirm")
    public CommonResponseDto<?> confirmDocument(
        @RequestBody DocumentConfirmRequestDto requestDto,
        HttpServletRequest request) {

        List<ConstraintViolation<DocumentConfirmRequestDto>> violations =
            this.validator.validate(requestDto).stream().collect(Collectors.toList());
        if (violations.stream().count() > 0) {
            return new CommonResponseDto(
                ResponseStatus.RES_CODE_SERVICE,
                "fail",
                new ViolationsMessageGenerator().getMessage(violations)
            );
        }

        if (!requestDto.getUserId().equals(authService.getUserIdFromJwtCookie(request))) {
            return new CommonResponseDto(
                ResponseStatus.RES_CODE_SERVICE,
                "일치하지 않는 회원인증정보 입니다.",
                null);
        }

        documentService.confirmDocument(requestDto);
        return new CommonResponseDto<>(
            ResponseStatus.RES_CODE_SUCCESS, "success", null);
    }

    @ApiOperation(value = "내가 생성한 문서 중 결재 진행 중인 문서", notes = "내가 생성한 문서 중 결재 진행 중인 문서를 조회한다.")
    @PostMapping("/selectOutBox")
    public CommonResponseDto<List<DocumentAggregationDto>> selectOutBox(
        @RequestBody SearchDocumentRequestDto requestDto,
        HttpServletRequest request) {

        List<ConstraintViolation<SearchDocumentRequestDto>> violations =
            this.validator.validate(requestDto).stream().collect(Collectors.toList());
        if (violations.stream().count() > 0) {
            return new CommonResponseDto(
                ResponseStatus.RES_CODE_SERVICE,
                "fail",
                new ViolationsMessageGenerator().getMessage(violations)
            );
        }

        if (!requestDto.getFindUserId().equals(authService.getUserIdFromJwtCookie(request))) {
            return new CommonResponseDto(
                ResponseStatus.RES_CODE_SERVICE,
                "일치하지 않는 회원인증정보 입니다.",
                null);
        }

        List<DocumentAggregationDto> result =  documentService.selectOutBox(requestDto);
        return new CommonResponseDto<>(
            ResponseStatus.RES_CODE_SUCCESS, "success", result
        );
    }

    @ApiOperation(value = "내가 결재를 해야 할 문서", notes = "내가 결재를 해야 할 문서를 조회한다.")
    @PostMapping("/selectInBox")
    public CommonResponseDto<List<DocumentAggregationDto>> selectInBox(
        @RequestBody SearchDocumentRequestDto requestDto,
        HttpServletRequest request) {

        List<ConstraintViolation<SearchDocumentRequestDto>> violations =
            this.validator.validate(requestDto).stream().collect(Collectors.toList());
        if (violations.stream().count() > 0) {
            return new CommonResponseDto(
                ResponseStatus.RES_CODE_SERVICE,
                "fail",
                new ViolationsMessageGenerator().getMessage(violations)
            );
        }

        if (!requestDto.getFindUserId().equals(authService.getUserIdFromJwtCookie(request))) {
            return new CommonResponseDto(
                ResponseStatus.RES_CODE_SERVICE,
                "일치하지 않는 회원인증정보 입니다.",
                null);
        }

        List<DocumentAggregationDto> result =  documentService.selectInBox(requestDto);
        return new CommonResponseDto<>(
            ResponseStatus.RES_CODE_SUCCESS, "success", result
        );
    }

    @ApiOperation(value = "내가 관여한 문서 중 결재가 완료(승인 또는 거절)된 문서", notes = "내가 관여된 결재문서를 조회한다.")
    @PostMapping("/selectArchive")
    public CommonResponseDto<List<DocumentAggregationDto>> selectArchive(
        @RequestBody DocumentPagingRequestDto requestDto,
        HttpServletRequest request) {

        List<ConstraintViolation<DocumentPagingRequestDto>> violations =
            this.validator.validate(requestDto).stream().collect(Collectors.toList());
        if (violations.stream().count() > 0) {
            return new CommonResponseDto(
                ResponseStatus.RES_CODE_SERVICE,
                "fail",
                new ViolationsMessageGenerator().getMessage(violations)
            );
        }

        if (!requestDto.getUserId().equals(authService.getUserIdFromJwtCookie(request))) {
            return new CommonResponseDto(
                ResponseStatus.RES_CODE_SERVICE,
                "일치하지 않는 회원인증정보 입니다.",
                null);
        }

        List<DocumentAggregationDto> result =  documentService.selectArchive(requestDto);
        return new CommonResponseDto<>(
            ResponseStatus.RES_CODE_SUCCESS, "success", result
        );
    }

}
