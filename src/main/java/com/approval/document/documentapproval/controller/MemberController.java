package com.approval.document.documentapproval.controller;

import com.approval.document.documentapproval.domain.entity.ResponseStatus;
import com.approval.document.documentapproval.domain.service.AuthService;
import com.approval.document.documentapproval.domain.service.CookieService;
import com.approval.document.documentapproval.domain.service.MemberService;
import com.approval.document.documentapproval.dto.CommonResponseDto;
import com.approval.document.documentapproval.dto.member.ValidMemberRequestDto;
import com.approval.document.documentapproval.dto.member.ValidMemberResponseDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;

    @Autowired
    public MemberController(MemberService memberService,
                            AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @ApiOperation(value = "회원 로그인", notes = "아이디와 비밀번호로 로그인을 수행한다. 세션에 대한 서버구성은 별도로 하지 않는다")
    @PostMapping("/userLogin")
    public CommonResponseDto<ValidMemberResponseDto> userLogin(
        @RequestBody ValidMemberRequestDto requestDto,
        @ApiIgnore HttpServletResponse response) throws UnsupportedEncodingException {

        if (!this.memberService.validUser(requestDto)) {
            return new CommonResponseDto<>(
                ResponseStatus.RES_CODE_SERVICE,
                "success",
                ValidMemberResponseDto.builder().validMemberMessage("유효한 로그인이 아닙니다.").build());
        }

        String userJwt = authService.issueToken(requestDto.getUserId());
        if (userJwt.isEmpty()) {
            return new CommonResponseDto<>(
                ResponseStatus.RES_CODE_SERVICE,
                "success",
                ValidMemberResponseDto.builder().validMemberMessage("유효한 로그인이 아닙니다.").build());
        }

        response.addCookie(CookieService.addCookie("userJwt", userJwt));

        return new CommonResponseDto<>(
            ResponseStatus.RES_CODE_SUCCESS,
            "success",
            ValidMemberResponseDto.builder()
                .userJwt(userJwt)
                .validMemberMessage("유효한 로그인 입니다.").build());
    }

    @ApiOperation(value = "회원 로그아웃", notes = "회원 로그아웃 세션에 대한 서버구성은 별도로 하지 않는다")
    @PostMapping("/userLogout")
    public CommonResponseDto userLogout(@ApiIgnore HttpServletResponse response) {
        Cookie deleteCookie = CookieService.deleteCookie("userJwt");
        response.addCookie(deleteCookie);

        return new CommonResponseDto<>(
            ResponseStatus.RES_CODE_SUCCESS,
            "success",
            null);
    }
}
