package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.config.security.AuthTokenFilter;
import com.approval.document.documentapproval.domain.entity.Member;
import com.approval.document.documentapproval.domain.entity.repository.MemberRepository;
import com.approval.document.documentapproval.dto.member.JoinMemberRequestDto;
import com.approval.document.documentapproval.dto.member.MemberResponseDto;
import com.approval.document.documentapproval.dto.member.ValidMemberRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final DocumentQueryGenerator queryGenerator;
    private final EntityManager entityManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    public MemberService(MemberRepository memberRepository,
                         DocumentQueryGenerator documentQueryGenerator,
                         EntityManager entityManager) {
        this.memberRepository = memberRepository;
        this.queryGenerator = documentQueryGenerator;
        this.entityManager = entityManager;
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
        Member member10 = this.queryGenerator.findMemberQuery();
        return member3;
    }

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
    public Member findMemberV2() throws InterruptedException {
        Member member1 = this.memberRepository.findById(1).get();
        member1.update("수정된 이름");
        entityManager.persist(member1);
        entityManager.setFlushMode(FlushModeType.COMMIT);
        entityManager.flush();
        System.out.println(String.format("findMemberV2 memberName: %s", member1.getMemberName()));
        findMemberV4();
        Thread.sleep(20000);
        return member1;
    }

    @Transactional(transactionManager = "easyTransactionManagerFactory", readOnly = false, isolation = Isolation.READ_COMMITTED)
    public Member findMemberV3() {
        Member member1 = this.memberRepository.findById(1).get();
        Member member2 = this.queryGenerator.findMemberTest();
        Member member3 = this.entityManager.find(Member.class, 1);
        System.out.println(String.format("findMemberV3 memberName: %s", member1.getMemberName()));
        System.out.println(String.format("findMemberV3 memberName: %s", member2.getMemberName()));
        System.out.println(String.format("findMemberV3 memberName: %s", member3.getMemberName()));
        return member1;
    }

    public Member findMemberV4() {
        Member member1 = this.memberRepository.findById(1).get();
        System.out.println(String.format("findMemberV4 memberName: %s", member1.getMemberName()));
        return member1;
    }


}
