package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.Member;
import com.approval.document.documentapproval.domain.entity.repository.MemberRepository;
import com.approval.document.documentapproval.dto.ValidMemberRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wildfly.common.Assert;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean validUser(ValidMemberRequestDto requestDto) {
        Member member = this.memberRepository
            .findByUserId(requestDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("not exists user"));

        return member.getUserPwd().equals(requestDto.getUserPwd());
    }
}
