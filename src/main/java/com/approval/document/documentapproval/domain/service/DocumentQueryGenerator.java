package com.approval.document.documentapproval.domain.service;

import com.approval.document.documentapproval.domain.entity.QApproval;
import com.approval.document.documentapproval.domain.entity.QEasyDocument;
import com.approval.document.documentapproval.dto.document.DocumentQueryDto;
import com.approval.document.documentapproval.dto.document.DocumentViewModel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class DocumentQueryGenerator {

    @PersistenceContext(unitName = "easy-master")
    private EntityManager entityManager;

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

    public List<DocumentViewModel> selectMyDocumentViewModel(String ownerId) {
        QEasyDocument qEasyDocument = new QEasyDocument("qed");
        QApproval qApproval = new QApproval("qa");

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(qEasyDocument.ownerId.eq(ownerId));
        whereClause.and(qApproval.isConfirm.eq(false));

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

    public List<DocumentViewModel> selectMyConfrimTargetDocumentViewModel(String approvalId) {
        QEasyDocument qEasyDocument = new QEasyDocument("qed");
        QApproval qApproval = new QApproval("qa");

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(qApproval.userId.eq(approvalId));

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

}
