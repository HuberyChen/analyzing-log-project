package com.quidsi.log.analyzing.dao;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
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

    public List<ActionLogDetail> getRecordsByLogId(int logId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("LogId", logId);
        sql.append("from ").append(ActionLogDetail.class.getName()).append(" where LogId = :LogId");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    public int getTotalCount(Date date, String status, int logId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(Id) ");
        this.pagingConfig(params, sql, date, status, logId);
        List<Long> result = jpaAccess.find(sql.toString(), params);
        if (CollectionUtils.isEmpty(result)) {
            return 0;
        }
        return result.get(0).intValue();
    }

    private void pagingConfig(Map<String, Object> params, StringBuilder sql, Date date, String status, int logId) {
        params.put("startDate", date);
        params.put("endDate", addDay(date));
        params.put("status", status);
        params.put("logId", logId);
        sql.append(" from ").append(ActionLogDetail.class.getName()).append(" where status = :status and  RecordTime > :startDate and RecordTime < :endDate and logId = :logId");
    }

    private Date addDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }


    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
