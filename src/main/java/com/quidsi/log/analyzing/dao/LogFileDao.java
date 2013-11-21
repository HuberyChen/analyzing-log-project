package com.quidsi.log.analyzing.dao;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.core.util.StringUtils;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.service.ServiceConstant;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LogFileDao {
    private JPAAccess jpaAccess;

    public int save(LogFile logFile) {
        jpaAccess.save(logFile);
        return logFile.getId();
    }

    public void update(LogFile logFile) {
        jpaAccess.update(logFile);
    }

    public LogFile getLogFilesByName(String logName, String project, String server) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("Project", project);
        params.put("Server", server);
        params.put("LogName", logName);
        sql.append("from ").append(LogFile.class.getName()).append(" where project = :Project and instance = :Server and LogName = :LogName");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    public List<LogFile> getLogFilesByFuzzyName(String logName, String project, String server) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("Project", project);
        params.put("Server", server);
        params.put("LogName", logName);
        sql.append("from ").append(LogFile.class.getName()).append(" where project =:Project and instance =:Server and charindex(:LogName,LogName) <> 0");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<LogFile> getLogFilesByCondition(String logName, String project, String server) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("LogName", logName);
        sql.append("from ").append(LogFile.class.getName()).append(" where charindex(:LogName,LogName) <> 0");
        if (!StringUtils.equals(ServiceConstant.TYPE_ALL, project)) {
            params.put("Project", project);
            sql.append(" and project =:Project");
        }
        if (!StringUtils.equals(ServiceConstant.TYPE_ALL, server)) {
            params.put("Server", server);
            sql.append(" and instance =:Server");
        }
        return jpaAccess.find(sql.toString(), params);
    }

    public List<LogFile> getLogFilesByLogType(String logType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("LogType", logType);
        sql.append("from ").append(LogFile.class.getName()).append(" where LogType = :LogType");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<LogFile> getLogFilesByProjectAndInstance(String project, String server) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("Project", project);
        params.put("Server", server);
        sql.append("from ").append(LogFile.class.getName()).append(" where project =:Project and instance =:Server");
        return jpaAccess.find(sql.toString(), params);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
