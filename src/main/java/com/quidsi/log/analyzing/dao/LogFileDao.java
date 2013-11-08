package com.quidsi.log.analyzing.dao;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.LogFile;

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
        params.put("LogName", "%" + logName + "%");
        sql.append("from ").append(LogFile.class.getName()).append(" where project =:Project and instance =:Server and LogName like :LogName");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<LogFile> getLogFileByLogType(String logType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("LogType", logType);
        sql.append("from ").append(LogFile.class.getName()).append(" where LogType = :LogType");
        return jpaAccess.find(sql.toString(), params);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
