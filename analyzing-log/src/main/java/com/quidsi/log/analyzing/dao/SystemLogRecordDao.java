package com.quidsi.log.analyzing.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
    public void saveList(List<SystemLogRecord> records) {
        jpaAccess.save(records);
    }

    public SystemLogRecord getRecordByLogTimeAndHost(Date logTime, String host) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("LogTime", logTime);
        params.put("Host", host);
        sql.append("from ").append(SystemLogRecord.class.getName()).append(" where LogTime = :LogTime and Host = :Host");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }

}
