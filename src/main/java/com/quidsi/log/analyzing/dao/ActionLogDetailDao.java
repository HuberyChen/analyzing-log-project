package com.quidsi.log.analyzing.dao;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.core.util.StringUtils;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
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

//    public List<ActionLogDetail> findConditionLimit(SearchDetailCondition searchDetailCondition) {
//        Map<String, Object> params = new HashMap<>();
//        StringBuilder sql = new StringBuilder();
//        conditionSql(params, sql, searchDetailCondition);
//        sql.append(" order by recordTime desc");
//        return jpaAccess.find(sql.toString(), params, searchDetailCondition.getOffset(), ServiceConstant.DEFAULTFETCHSIZE);
//    }

    public List<ActionLogDetail> findConditionLimit(SearchDetailCondition searchDetailCondition) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        findConditionLimit(params, sql, searchDetailCondition);
    }

    private void findConditionLimitId(Map<String, Object> params, StringBuilder sql, SearchDetailCondition searchDetailCondition) {
        sql.append("select detail.id");
        conditionSql(params, sql, searchDetailCondition);
        sql.append(" order by recordTime desc");
    }

    public List<ActionLogDetail> getRecordsByLogId(int logId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("LogId", logId);
        sql.append("from ").append(ActionLogDetail.class.getName()).append(" where logId = :LogId");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    public int getTotalCountByCondition(SearchDetailCondition searchDetailCondition) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(Id) ");
        conditionSql(params, sql, searchDetailCondition);
        List<Long> result = jpaAccess.find(sql.toString(), params);
        if (CollectionUtils.isEmpty(result)) {
            return 0;
        }
        return result.get(0).intValue();
    }

    private void conditionLimit(Map<String, Object> params, StringBuilder sql, SearchDetailCondition searchDetailCondition) {
        if (StringUtils.hasText(searchDetailCondition.getInterfaceName())) {
            params.put("interfaceName", searchDetailCondition.getInterfaceName());
            sql.append(" and charindex(:interfaceName,detail.interface) <> 0");
        }
        if (StringUtils.hasText(searchDetailCondition.getStatus())) {
            params.put("status", searchDetailCondition.getStatus());
            sql.append(" and detail.status = :status");
        }
        if (StringUtils.hasText(searchDetailCondition.getErrorCode())) {
            params.put("errorCode", searchDetailCondition.getErrorCode());
            sql.append(" and detail.errorCode = :errorCode");
        }
    }

    private void logIdLimit(Map<String, Object> params, StringBuilder sql, List<Integer> logIdList) {
        sql.append("select 1 from ").append(ActionLogDetail.class.getName());
        if (CollectionUtils.isEmpty(logIdList)) {
            params.put("logIdList", null);
        } else {
            params.put("logIdList", logIdList);
        }
        sql.append(" where detail.logId in (:logIdList)");
    }

    private void conditionSql(Map<String, Object> params, StringBuilder sql, SearchDetailCondition searchDetailCondition) {
        sql.append(" from ").append(ActionLogDetail.class.getName()).append(" detail").append(" where exists (");
        logIdLimit(params, sql, searchDetailCondition.getLogIdList());
        conditionLimit(params, sql, searchDetailCondition);
        sql.append(")");
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
