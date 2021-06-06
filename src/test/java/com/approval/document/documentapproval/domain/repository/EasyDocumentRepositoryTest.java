package com.approval.document.documentapproval.domain.repository;

import com.approval.document.documentapproval.domain.entity.Approval;
import com.approval.document.documentapproval.domain.entity.DocumentType;
import com.approval.document.documentapproval.domain.entity.EasyDocument;
import com.approval.document.documentapproval.domain.entity.repository.ApprovalRepository;
import com.approval.document.documentapproval.domain.entity.repository.EasyDocumentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EasyDocumentRepositoryTest {

    @Autowired
    private EasyDocumentRepository easyDocumentRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Test
    public void given_approval_data_then_repository_should_save_it_correctly() {
        // arrange
        var document = EasyDocument.builder()
            .title("구매요청서 입니다")
            .content("도서 블라블라 구매요청 드립니다.")
            .type(DocumentType.PRODUCT_PURCHASE)
            .build();

        // act
        Integer documentId = easyDocumentRepository.save(document).getDocumentId();
        Approval approval = approvalRepository.save(Approval.builder()
            .userId("easywaldo")
            .documentId(documentId)
            .order(1)
            .build());

        // assert
        var result = easyDocumentRepository.findById(documentId).get();
        var approvalResult = approvalRepository.findById(approval.getApprovalId()).get();
        assertThat(result.getTitle()).isEqualTo(document.getTitle());
        assertThat(approvalResult.getUserId()).isEqualTo(approval.getUserId());
    }
}
