package com.quidsi.log.analyzing.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.LogFile;

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

    public LogFile getLogFilesByName(String logName, int projectId, int serverId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("ProjectId", projectId);
        params.put("ServerId", serverId);
        params.put("LogName", logName);
        sql.append("from ").append(LogFile.class.getName()).append(" where ProjectId = :ProjectId and ServerId = :ServerId and LogName = :LogName");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    public List<LogFile> getLogFilesByFuzzyName(String logName, int projectId, int serverId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("ProjectId", projectId);
        params.put("ServerId", serverId);
        params.put("LogName", "%" + logName + "%");
        sql.append("from ").append(LogFile.class.getName()).append(" where ProjectId = :ProjectId and ServerId = :ServerId and LogName like :LogName");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<LogFile> getUncompressedLogFilesByFuzzyName(String logName, int projectId, int serverId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("ProjectId", projectId);
        params.put("ServerId", serverId);
        params.put("LogName", "%" + logName + "%");
        sql.append("from ").append(LogFile.class.getName()).append(" where ProjectId = :ProjectId and ServerId = :ServerId and LogName like :LogName and IsDecomposed = 'N'");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<LogFile> getAnalyzedLogFilesByFuzzyName(String logName, int projectId, int serverId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("ProjectId", projectId);
        params.put("ServerId", serverId);
        params.put("LogName", "%" + logName + "%");
        sql.append("from ").append(LogFile.class.getName()).append(" where ProjectId = :ProjectId and ServerId = :ServerId and LogName like :LogName and IsAnalyzed = 'N'");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<LogFile> getLogFileByLogType(String logType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("LogType", logType);
        sql.append("from ").append(LogFile.class.getName()).append(" where LogType = :LogType");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<LogFile> getUncompressedLogFilesByLogFileWrapper(String isDecomposed) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("IsDecomposed", isDecomposed);
        sql.append("from ").append(LogFile.class.getName()).append(" where IsDecomposed = :IsDecomposed");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<LogFile> getLogFilesByIsAnalyzed(String isAnalyzed) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("IsAnalyzed", isAnalyzed);
        sql.append("from ").append(LogFile.class.getName()).append(" where IsAnalyzed = :IsAnalyzed");
        return jpaAccess.find(sql.toString(), params);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
