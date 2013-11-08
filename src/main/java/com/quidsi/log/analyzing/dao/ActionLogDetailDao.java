package com.quidsi.log.analyzing.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.ActionLogDetail;

@Repository
public class ActionLogDetailDao {

    private JPAAccess jpaAccess;

    public int save(ActionLogDetail record) {
        jpaAccess.save(record);
        return record.getId();
    }

    public List<ActionLogDetail> getRecordsByLogId(int logId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("LogId", logId);
        sql.append("from ").append(ActionLogDetail.class.getName()).append(" where LogId = :LogId");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
