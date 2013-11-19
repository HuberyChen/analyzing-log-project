package com.quidsi.log.analyzing.dao;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.service.ServiceConstant;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ActionLogDetailDao {

    private JPAAccess jpaAccess;

    public int save(ActionLogDetail record) {
        jpaAccess.save(record);
        return record.getId();
    }

    public List<ActionLogDetail> findList(List<Integer> logIdList, int offset) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        this.pagingConfig(params, sql, logIdList);
        return jpaAccess.find(sql.toString(), params, offset, ServiceConstant.DEFAULTFETCHSIZE);
    }

    public List<ActionLogDetail> getRecordsByLogId(int logId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("LogId", logId);
        sql.append("from ").append(ActionLogDetail.class.getName()).append(" where LogId = :LogId");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    public int getTotalCount(List<Integer> logIdList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(Id) ");
        this.pagingConfig(params, sql, logIdList);
        List<Long> result = jpaAccess.find(sql.toString(), params);
        if (CollectionUtils.isEmpty(result)) {
            return 0;
        }
        return result.get(0).intValue();
    }

    private void pagingConfig(Map<String, Object> params, StringBuilder sql, List<Integer> logIdList) {
        sql.append(" from ").append(ActionLogDetail.class.getName());
        if (!CollectionUtils.isEmpty(logIdList)) {
            sql.append(" where logId in (");
            for (Integer logId : logIdList) {
                params.put("logId" + logId.toString(), logId);
                sql.append(" :logId" + logId.toString() + ",");
            }
            sql.append(" null )");
        }
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
