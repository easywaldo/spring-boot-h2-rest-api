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

    @ApiOperation(value = "unCommittedTest Test", notes = "")
    @GetMapping(value ="unCommittedTest")
    public void unCommittedTest() throws InterruptedException {
        this.memberService.findMemberV2();
    }

    @ApiOperation(value = "unCommittedTest Test", notes = "")
    @GetMapping(value ="unCommittedReadTest")
    public void unCommittedReadTest() {
        this.memberService.findMemberV3();
    }

    @ApiOperation(value = "bulkInsert Test", notes = "")
    @GetMapping(value ="bulkInsert")
    public void bulkInsert() {
        this.memberService.bulkMember();
    }


}
