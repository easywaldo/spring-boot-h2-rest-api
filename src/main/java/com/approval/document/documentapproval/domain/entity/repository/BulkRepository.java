package com.approval.document.documentapproval.domain.entity.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.Serializable;

@Transactional
public class BulkRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BatchRepository<T, ID> {

    private final BatchExecutor batchExecutor;

    @Autowired
    public BulkRepository(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        batchExecutor = new BatchExecutor(em.getEntityManagerFactory());
    }

    @Override
    public <S extends T> void saveInBatch(Iterable<S> entitles) {
        batchExecutor.saveInBatch(entitles);
    }
}
