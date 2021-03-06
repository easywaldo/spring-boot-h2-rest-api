package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.*;
import com.approval.document.documentapproval.domain.entity.repository.EasyDocumentRepository;
import com.approval.document.documentapproval.dto.document.DocumentAggregationDto;
import com.approval.document.documentapproval.dto.document.DocumentPagingRequestDto;
import com.approval.document.documentapproval.dto.document.DocumentQueryDto;
import com.approval.document.documentapproval.dto.document.DocumentViewModel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentQueryGenerator {

    private final JPAQueryFactory queryFactory;
    private final EasyDocumentRepository easyDocumentRepository;
    //@PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public DocumentQueryGenerator(EntityManager entityManager,
                                  EasyDocumentRepository easyDocumentRepository) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.easyDocumentRepository = easyDocumentRepository;
        this.entityManager = entityManager;
    }

    public List<DocumentViewModel> selectDocumentViewModel(DocumentQueryDto queryDto) {
        QEasyDocument qEasyDocument = new QEasyDocument("qed");
        QApproval qApproval = new QApproval("qa");

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(qEasyDocument.documentId.eq(queryDto.getDocumentId()));

        if (queryDto.getApprovalId() > 0  && !queryDto.isPrevCheck() && !queryDto.isLastCheck()) {
            whereClause.and(qApproval.approvalId.lt(queryDto.getApprovalId()));
            whereClause.and(qApproval.isConfirm.eq(false));
        }

        if (queryDto.getApprovalId() > 0 && queryDto.isPrevCheck() && !queryDto.isLastCheck()) {
            whereClause.and(qApproval.approvalId.lt(queryDto.getApprovalId()));
            whereClause.and(qApproval.isApproved.eq(false));
        }

        if (queryDto.getApprovalId() > 0 && queryDto.isLastCheck()) {
            whereClause.and(qApproval.approvalId.gt(queryDto.getApprovalId()));
        }

        List<DocumentViewModel> resultModel = this.queryFactory.from(qEasyDocument)
            .join(qApproval).on(qApproval.documentId.eq(qEasyDocument.documentId))
            .where(whereClause)
            .select(Projections.fields(DocumentViewModel.class,
                qEasyDocument.documentId,
                qEasyDocument.title,
                qEasyDocument.content,
                qEasyDocument.type,
                qApproval.approvalId,
                qApproval.userId,
                qApproval.isConfirm,
                qApproval.isApproved))
            .fetch();

        return resultModel;
    }

    /**
     * ?????? ????????? ?????? ??? ?????? ?????? ?????? ??????
     * @param ownerId
     * @return
     */
    public List<DocumentAggregationDto> selectOutBox(String ownerId) {
        QEasyDocument qEasyDocument = new QEasyDocument("qed");
        QApproval qApproval = new QApproval("qa");

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(qEasyDocument.ownerId.eq(ownerId));
        whereClause.and(qEasyDocument.documentStatus.eq(DocumentStatus.ING));

        List<DocumentViewModel> resultModel = this.queryFactory
            .from(qEasyDocument)
            .join(qApproval).on(qApproval.documentId.eq(qEasyDocument.documentId))
            .where(whereClause)
            .select(Projections.fields(DocumentViewModel.class,
                qEasyDocument.documentId,
                qEasyDocument.ownerId,
                qEasyDocument.title,
                qEasyDocument.content,
                qEasyDocument.type,
                qEasyDocument.documentStatus,
                qApproval.approvalId,
                qApproval.userId,
                qApproval.isConfirm,
                qApproval.isApproved))
            .fetch();

        return convertAggregation(resultModel);
    }

    /**
     * ?????? ????????? ?????? ??? ??????
     * @param approvalId
     * @return
     */
    public List<DocumentAggregationDto> selectInBox(String approvalId) {
        QEasyDocument qEasyDocument = new QEasyDocument("qed");
        QApproval qApproval = new QApproval("qa");

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(qApproval.userId.eq(approvalId));
        whereClause.and(qEasyDocument.documentStatus.eq(DocumentStatus.ING));
        whereClause.and(qApproval.isConfirm.eq(false));

        List<DocumentViewModel> resultModel = this.queryFactory.from(qEasyDocument)
            .join(qApproval).on(qApproval.documentId.eq(qEasyDocument.documentId))
            .where(whereClause)
            .select(Projections.fields(DocumentViewModel.class,
                qEasyDocument.documentId,
                qEasyDocument.ownerId,
                qEasyDocument.title,
                qEasyDocument.content,
                qEasyDocument.type,
                qEasyDocument.documentStatus,
                qApproval.approvalId,
                qApproval.userId,
                qApproval.isConfirm,
                qApproval.isApproved))
            .fetch();

        return convertAggregation(resultModel);
    }

    /**
     * ?????? ????????? ?????? ??? ????????? ??????(?????? ?????? ??????)??? ????????? ????????????.
     * @param requestDto
     * @return
     */
    public List<DocumentAggregationDto> selectArchive(DocumentPagingRequestDto requestDto) {
        QEasyDocument qEasyDocument = new QEasyDocument("qed");
        QApproval qApproval = new QApproval("qa");

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.andAnyOf(qApproval.userId.eq(requestDto.getUserId()),qEasyDocument.ownerId.eq(requestDto.getUserId()));
        whereClause.and(qEasyDocument.documentStatus.ne(DocumentStatus.ING));

        List<Integer> documentIdList = this.queryFactory.from(qEasyDocument)
            .join(qApproval).on(qApproval.documentId.eq(qEasyDocument.documentId))
            .where(whereClause)
            .groupBy(qEasyDocument.documentId)
            .offset(requestDto.getOffset())
            .limit(requestDto.getLimit())
            .select(qEasyDocument.documentId)
            .fetch();

        List<DocumentViewModel> resultModel = this.queryFactory.from(qEasyDocument)
            .where(qEasyDocument.documentId.in(documentIdList))
            .select(Projections.fields(DocumentViewModel.class,
                qEasyDocument.documentId,
                qEasyDocument.ownerId,
                qEasyDocument.title,
                qEasyDocument.content,
                qEasyDocument.type,
                qEasyDocument.documentStatus))
            .fetch();

        return convertAggregation(resultModel);
    }

    private List<DocumentAggregationDto> convertAggregation(List<DocumentViewModel> viewModel) {
        var documentApproval = viewModel.stream()
            .collect(Collectors.groupingBy(g -> g.getDocumentId(), Collectors.toList()));
        List<DocumentAggregationDto> documentAggregationDto = new ArrayList<>();
        for (var key : documentApproval.keySet()) {
            DocumentViewModel keyInfo = documentApproval.get(key).stream().findFirst().get();
            documentAggregationDto.add(DocumentAggregationDto.builder()
                .documentId(key)
                .documentStatus(keyInfo.getDocumentStatus())
                .content(keyInfo.getContent())
                .title(keyInfo.getTitle())
                .type(keyInfo.getType())
                .ownerId(keyInfo.getOwnerId())
                .approvalLine(easyDocumentRepository.findById(key).get()
                    .getApprovalList()
                    .stream().map(line -> DocumentAggregationDto.ApprovalDto.builder()
                        .approvalId(line.getApprovalId())
                        .userId(line.getUserId())
                        .isConfirm(line.isConfirm())
                        .isApproved(line.isApproved())
                        .build())
                    .collect(Collectors.toList()))
                .build());
        }
        return documentAggregationDto;
    }

    public Member findMemberTest() {
        Member result = (Member)this.entityManager.find(Member.class, 1);
        return result;
    }

    public Member findMemberTestV2() {
        Member result = (Member)this.entityManager.createQuery(
            "select m from Member m where m.userId = 'easywaldo'").getSingleResult();
        return result;
    }

    public Member findMemberQuery() {
        var result = queryFactory.from(QMember.member)
            .where(QMember.member.memberSeq.eq(1))
            .select(Projections.fields(Member.class,
                QMember.member.memberSeq,
                QMember.member.memberName,
                QMember.member.userId,
                QMember.member.userPwd
                ))
            .setLockMode(LockModeType.WRITE).fetchOne();

        result.update(String.format("%s-modifiedName", result.getMemberName()));

        return result;
    }

}
