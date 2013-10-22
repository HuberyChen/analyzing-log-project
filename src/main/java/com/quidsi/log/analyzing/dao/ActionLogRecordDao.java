package com.quidsi.log.analyzing.dao;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.ActionLogRecord;

@Repository
public class ActionLogRecordDao {

    private JPAAccess jpaAccess;

    public int save(ActionLogRecord record) {
        jpaAccess.save(record);
        return record.getId();
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
