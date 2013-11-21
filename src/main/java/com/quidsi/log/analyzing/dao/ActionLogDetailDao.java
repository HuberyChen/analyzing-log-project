package com.quidsi.log.analyzing.dao;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.core.util.StringUtils;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
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

    public List<ActionLogDetail> findConditionLimit(SearchDetailCondition searchDetailCondition) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        this.logIdLimit(params, sql, searchDetailCondition.getLogIdList());
        this.conditionLimit(params, sql, searchDetailCondition);
        return jpaAccess.find(sql.toString(), params, searchDetailCondition.getOffset(), ServiceConstant.DEFAULTFETCHSIZE);
    }

    public List<ActionLogDetail> findList(List<Integer> logIdList, int offset) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        this.logIdLimit(params, sql, logIdList);
        return jpaAccess.find(sql.toString(), params, offset, ServiceConstant.DEFAULTFETCHSIZE);
    }

    public List<ActionLogDetail> getRecordsByLogId(int logId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("LogId", logId);
        sql.append("from ").append(ActionLogDetail.class.getName()).append(" where LogId = :LogId");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    public int getTotalCountByCondition(SearchDetailCondition searchDetailCondition) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(Id) ");
        this.logIdLimit(params, sql, searchDetailCondition.getLogIdList());
        this.conditionLimit(params, sql, searchDetailCondition);
        List<Long> result = jpaAccess.find(sql.toString(), params);
        if (CollectionUtils.isEmpty(result)) {
            return 0;
        }
        return result.get(0).intValue();
    }

    public int getTotalCount(List<Integer> logIdList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(Id) ");
        this.logIdLimit(params, sql, logIdList);
        List<Long> result = jpaAccess.find(sql.toString(), params);
        if (CollectionUtils.isEmpty(result)) {
            return 0;
        }
        return result.get(0).intValue();
    }

//    private void conditionLimit(Map<String, Object> params, StringBuilder sql, String condition) {
//        params.put("condition", "%" + condition + "%");
//        sql.append(" and ( Interface like :condition or Status like :condition or RequestMethod like :condition )");
//    }

//    private void conditionLimit(Map<String, Object> params, StringBuilder sql, String condition) {
//        params.put("condition", condition);
//        sql.append(" and ( charindex(:condition,Interface) <> 0 or charindex(:condition,Status) <> 0 or charindex(:condition,RequestMethod) <> 0 )");
//    }

    private void conditionLimit(Map<String, Object> params, StringBuilder sql, SearchDetailCondition searchDetailCondition) {
        if (StringUtils.hasText(searchDetailCondition.getInterfaceName())) {
            params.put("interfaceName", searchDetailCondition.getInterfaceName());
            sql.append(" and Interface = :interfaceName");
        }
        if (StringUtils.hasText(searchDetailCondition.getStatus())) {
            params.put("status", searchDetailCondition.getStatus());
            sql.append(" and Status = :status");
        }
        if (StringUtils.hasText(searchDetailCondition.getErrorCode())) {
            params.put("errorCode", searchDetailCondition.getErrorCode());
            sql.append(" and errorCode = :errorCode");
        }
    }

    private void logIdLimit(Map<String, Object> params, StringBuilder sql, List<Integer> logIdList) {
        sql.append(" from ").append(ActionLogDetail.class.getName());
        sql.append(" where logId in (");
        if (!CollectionUtils.isEmpty(logIdList)) {
            for (Integer logId : logIdList) {
                params.put("logId" + logId.toString(), logId);
                sql.append(" :logId" + logId.toString() + ",");
            }
        }
        sql.append(" null )");
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
