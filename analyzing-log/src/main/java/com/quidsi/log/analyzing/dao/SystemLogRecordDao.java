package com.quidsi.log.analyzing.dao;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.SystemLogRecord;

@Repository
public class SystemLogRecordDao {

    private JPAAccess jpaAccess;

    @Transactional
    public int save(SystemLogRecord systemLogRecord) {
        jpaAccess.save(systemLogRecord);
        return systemLogRecord.getId();
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }

}
