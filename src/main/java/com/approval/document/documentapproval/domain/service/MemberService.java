package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.Member;
import com.approval.document.documentapproval.domain.entity.repository.MemberRepository;
import com.approval.document.documentapproval.dto.member.JoinMemberRequestDto;
import com.approval.document.documentapproval.dto.member.MemberResponseDto;
import com.approval.document.documentapproval.dto.member.ValidMemberRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final DocumentQueryGenerator queryGenerator;

    @Autowired
    public MemberService(MemberRepository memberRepository,
                         DocumentQueryGenerator documentQueryGenerator) {
        this.memberRepository = memberRepository;
        this.queryGenerator = documentQueryGenerator;
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

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
    public Member findMember() {
        Member member1 = this.memberRepository.findById(1).get();
        Member member2 = this.memberRepository.findByUserId("easywaldo").get();
        Member member3 = this.queryGenerator.findMemberTest();
        Member member4 = this.memberRepository.findById(1).get();
        Member member5 = this.memberRepository.jpqlFindUserId("easywaldo").get();
        Member member6 = this.queryGenerator.findMemberTest();
        Member member7 = this.queryGenerator.findMemberTestV2();
        Member member8 = this.queryGenerator.findMemberTestV2();
        Member member9 = this.queryGenerator.findMemberQuery();
        return member3;
    }
}
