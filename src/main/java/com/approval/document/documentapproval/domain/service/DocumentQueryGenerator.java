package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.DocumentStatus;
import com.approval.document.documentapproval.domain.entity.QApproval;
import com.approval.document.documentapproval.domain.entity.QEasyDocument;
import com.approval.document.documentapproval.dto.document.DocumentAggregationDto;
import com.approval.document.documentapproval.dto.document.DocumentQueryDto;
import com.approval.document.documentapproval.dto.document.DocumentViewModel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentQueryGenerator {

    /*@PersistenceContext(unitName = "easy-master")
    private EntityManager entityManager;*/

    private final JPAQueryFactory queryFactory;

    @Autowired
    public DocumentQueryGenerator(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
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
     * 내가 생성한 문서 중 결재 진행 중인 문서
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
     * 내가 결재를 해야 할 문서
     * @param approvalId
     * @return
     */
    public List<DocumentAggregationDto> selectInBox(String approvalId) {
        QEasyDocument qEasyDocument = new QEasyDocument("qed");
        QApproval qApproval = new QApproval("qa");

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(qApproval.userId.eq(approvalId));

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
     * 내가 관여한 문서 중 결재가 완료(승인 또는 거절)된 문서를 조회한다.
     * @param myUserId
     * @return
     */
    public List<DocumentAggregationDto> selectArchive(String myUserId) {
        QEasyDocument qEasyDocument = new QEasyDocument("qed");
        QApproval qApproval = new QApproval("qa");

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.andAnyOf(qApproval.userId.eq(myUserId),qEasyDocument.ownerId.eq(myUserId));
        whereClause.and(qEasyDocument.documentStatus.ne(DocumentStatus.ING));

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

    private List<DocumentAggregationDto> convertAggregation(List<DocumentViewModel> viewModel) {
        var documentApproval = viewModel.stream()
            .collect(Collectors.groupingBy(g -> g.getDocumentId(), Collectors.toList()));
        List<DocumentAggregationDto> documentAggregationDto = new ArrayList<>();
        for (var key : documentApproval.keySet()) {
            documentAggregationDto.add(DocumentAggregationDto.builder()
                .documentId(key)
                .documentStatus(documentApproval.get(key).stream().findFirst().get().getDocumentStatus())
                .content(documentApproval.get(key).stream().findFirst().get().getContent())
                .title(documentApproval.get(key).stream().findFirst().get().getTitle())
                .type(documentApproval.get(key).stream().findFirst().get().getType())
                .ownerId(documentApproval.get(key).stream().findFirst().get().getOwnerId())
                .approvalList(documentApproval.get(key)
                    .stream()
                    .map(m ->
                        DocumentAggregationDto.ApprovalDto.builder()
                            .approvalId(m.getApprovalId())
                            .userId(m.getUserId())
                            .isConfirm(m.isConfirm())
                            .isApproved(m.isApproved())
                            .build()
                    ).collect(Collectors.toList()))
                .build());
        }
        return documentAggregationDto;
    }

}
