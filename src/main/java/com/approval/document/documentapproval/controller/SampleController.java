package com.approval.document.documentapproval.controller;

import com.approval.document.documentapproval.domain.entity.Member;
import com.approval.document.documentapproval.domain.service.MemberService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    private final MemberService memberService;

    @Autowired
    public SampleController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "회원 테스트 조회", notes = "")
    @GetMapping(value ="repeatableTest")
    public void repeatableReadTest() {
        Member member = this.memberService.findMember();

    }
}
