package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.Member;
import com.approval.document.documentapproval.domain.entity.repository.MemberRepository;
import com.approval.document.documentapproval.dto.member.JoinMemberRequestDto;
import com.approval.document.documentapproval.dto.member.ValidMemberRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = true)
    public boolean validUser(ValidMemberRequestDto requestDto) {
        Member member = this.memberRepository
            .findByUserId(requestDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("not exists user"));

        return member.getUserPwd().equals(requestDto.getUserPwd());
    }

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = false)
    public int joinUser(JoinMemberRequestDto joinUser) {
        return this.memberRepository.save(joinUser.toEntity()).getMemberSeq();
    }
}
