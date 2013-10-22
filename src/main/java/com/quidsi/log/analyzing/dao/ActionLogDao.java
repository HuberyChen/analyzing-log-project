package com.quidsi.log.analyzing.dao;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.ActionLog;

@Repository
public class ActionLogDao {

    private JPAAccess jpaAccess;

    public int save(ActionLog actionLog) {
        jpaAccess.save(actionLog);
        return actionLog.getId();
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
