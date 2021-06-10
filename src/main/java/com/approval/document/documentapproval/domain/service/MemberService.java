package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.Member;
import com.approval.document.documentapproval.domain.entity.repository.MemberRepository;
import com.approval.document.documentapproval.dto.member.JoinMemberRequestDto;
import com.approval.document.documentapproval.dto.member.MemberResponseDto;
import com.approval.document.documentapproval.dto.member.ValidMemberRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        return member.getUserPwd().equals(SHAEncryptServiceImpl.getSHA512(requestDto.getUserPwd()));
    }

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = false)
    public int joinUser(JoinMemberRequestDto joinUser) {
        return this.memberRepository.save(joinUser.toEntity()).getMemberSeq();
    }

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = true)
    public List<MemberResponseDto> findUserList() {
        return this.memberRepository.findAll()
            .stream()
            .map(x -> MemberResponseDto.builder()
            .userId(x.getUserId())
            .memberName(x.getMemberName())
            .build())
            .collect(Collectors.toList());
    }
}
