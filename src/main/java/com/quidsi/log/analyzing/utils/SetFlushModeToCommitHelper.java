package com.quidsi.log.analyzing.utils;

import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;

@Service
@Singleton
public class SetFlushModeToCommitHelper {

    @Inject
    private EntityManagerFactory entityManagerFactory;

    public void setFlushModeToCommit() {
        EntityManagerHolder entityManagerHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(entityManagerFactory);
        if (entityManagerHolder != null && entityManagerHolder.getEntityManager() != null) {
            entityManagerHolder.getEntityManager().setFlushMode(FlushModeType.COMMIT);
        }
    }

}
