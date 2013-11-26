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
        sql.append("from ").append(LogFile.class.getName()).append(" where project = :Project and instance = :Server and logName = :LogName");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    public List<LogFile> getLogFilesByFuzzyName(String logName, String project, String server) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("Project", project);
        params.put("Server", server);
        params.put("LogName", logName);
        sql.append("from ").append(LogFile.class.getName()).append(" where project =:Project and instance =:Server and charindex(:LogName,logName) <> 0");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<LogFile> getLogFilesByCondition(String logName, String project, String server) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("Project", project);
        params.put("Server", server);
        params.put("LogName", logName);
        params.put("IsAnalyzed", LogFile.IsAnalyzed.Y);
        sql.append("from ").append(LogFile.class.getName()).append(" where project =:Project and instance =:Server and charindex(:LogName,logName) <> 0 and isAnalyzed =:IsAnalyzed");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<LogFile> getLogFilesByLogType(String logType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("LogType", logType);
        sql.append("from ").append(LogFile.class.getName()).append(" where logType = :LogType");
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
