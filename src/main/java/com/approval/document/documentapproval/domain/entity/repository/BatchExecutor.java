package com.approval.document.documentapproval.domain.entity.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.logging.Level;

@Component
public class BatchExecutor<T> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private int batchSize = 10;
    private final EntityManagerFactory entityManagerFactory;

    public BatchExecutor(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    public <S extends T> void saveInBatch(Iterable<S> entities) {
        EntityManager entityManager
            = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            int i = 0;
            for (S entity : entities) {
                if (i % batchSize == 0 && i > 0) {
                    logger.info(String.format("Flushing the EntityManager containing %s entities ...", i));
                    entityTransaction.commit();
                    entityTransaction.begin();
                    entityManager.clear();
                }
                entityManager.persist(entity);
                i++;
            }
            logger.info("Flushing the remaining entities ...");
            entityTransaction.commit();
        } catch (RuntimeException e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
