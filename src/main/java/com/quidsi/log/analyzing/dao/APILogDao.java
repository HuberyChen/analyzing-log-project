package com.quidsi.log.analyzing.dao;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.APILog;

@Repository
public class APILogDao {

    private JPAAccess jpaAccess;

    public int save(APILog apiLog) {
        jpaAccess.save(apiLog);
        return apiLog.getId();
    }

    public APILog getApiLogsByName(String logName, String apiName, String hostName) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("APIName", apiName);
        params.put("HostName", hostName);
        params.put("LogName", logName);
        sql.append("from ").append(APILog.class.getName()).append(" where APIName = :APIName and HostName = :HostName and LogName = :LogName");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
