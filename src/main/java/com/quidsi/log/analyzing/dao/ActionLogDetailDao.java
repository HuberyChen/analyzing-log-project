package com.quidsi.log.analyzing.dao;

import com.quidsi.core.database.EntityRowMapper;
import com.quidsi.core.database.JDBCAccess;
import com.quidsi.core.database.JPAAccess;
import com.quidsi.core.util.StringUtils;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
import com.quidsi.log.analyzing.service.ServiceConstant;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ActionLogDetailDao {

    private JPAAccess jpaAccess;
    private JDBCAccess jdbcAccess;

    public int save(ActionLogDetail record) {
        jpaAccess.save(record);
        return record.getId();
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
        List<Object> params = new ArrayList<>();
        sql.append(" select count(t1.Id) ");
        conditionSql(params, sql, searchDetailCondition);
        return jdbcAccess.findInteger(sql.toString(), params.toArray());
    }

    public List<ActionLogDetail> findConditionLimit(SearchDetailCondition searchDetailCondition) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        findConditionLimitId(params, sql, searchDetailCondition);
        sql.append(" SELECT t1.* FROM Action_Log_Detail t1 INNER JOIN #tmplog t2 ON t1.id=t2.Id");
        sql.append(" where t2.timeId>").append(searchDetailCondition.getOffset());
        sql.append(" and t2.timeId<").append(searchDetailCondition.getOffset() + ServiceConstant.DEFAULTFETCHSIZE);
        sql.append(" ORDER BY t2.timeId ");
        return jdbcAccess.find(sql.toString(), EntityRowMapper.rowMapper(ActionLogDetail.class), params.toArray());
    }

    private void findConditionLimitId(List<Object> params, StringBuilder sql, SearchDetailCondition searchDetailCondition) {
        sql.append(" select row_number() over(order by RecordTime DESC) timeId,t1.ID, t1.LogId INTO #tmplog");
        conditionSql(params, sql, searchDetailCondition);
    }

    private void conditionSql(List<Object> params, StringBuilder sql, SearchDetailCondition searchDetailCondition) {
        sql.append(" from Action_Log_Detail t1");
        sql.append(" INNER JOIN Temp_Log_Id t2 ON t1.LogId=t2.LogId where 1=1");
        conditionLimit(params, sql, searchDetailCondition);
    }

    private void conditionLimit(List<Object> params, StringBuilder sql, SearchDetailCondition searchDetailCondition) {
        if (StringUtils.hasText(searchDetailCondition.getInterfaceName())) {
            params.add(searchDetailCondition.getInterfaceName());
            sql.append(" and charindex(t1.interface,?) <> 0");
        }
        if (StringUtils.hasText(searchDetailCondition.getStatus())) {
            params.add(searchDetailCondition.getStatus());
            sql.append(" and t1.status = ?");
        }
        if (StringUtils.hasText(searchDetailCondition.getErrorCode())) {
            params.add(searchDetailCondition.getErrorCode());
            sql.append(" and t1.errorCode = ?");
        }
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }

    @Inject
    public void setJdbcAccess(JDBCAccess jdbcAccess) {
        this.jdbcAccess = jdbcAccess;
    }
}
