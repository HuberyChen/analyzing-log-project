package com.quidsi.log.analyzing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quidsi.log.analyzing.dao.LogFileDao;
import com.quidsi.log.analyzing.domain.LogFile;

@Service
public class LogFileService {

    private LogFileDao logFileDao;

    @Transactional
    public int save(LogFile logFile) {
        return logFileDao.save(logFile);
    }

    @Transactional
    public void update(LogFile logFile) {
        logFileDao.update(logFile);
    }

    public List<LogFile> getLogFilesByType(String logType) {
        return logFileDao.getLogFileByLogType(logType);
    }

    public LogFile getLogFileByName(String logName, int projectId, int serverId) {
        return logFileDao.getLogFilesByName(logName, projectId, serverId);
    }

    public Map<String, List<String>> initializeFilters(String projectName, String serverName, String date) {
        Map<String, List<String>> filterMap = new HashMap<>();

        List<String> pathFilters = new ArrayList<>();
        pathFilters.add("[\\S]*" + projectName + "[\\S]*");
        pathFilters.add("[\\S]*" + serverName + "[\\S]*");
        filterMap.put("pathFilters", pathFilters);

        List<String> nameFilters = new ArrayList<>();
        nameFilters.add("[\\S]*.log[\\S]*");
        nameFilters.add("[\\S]*" + date + "[\\S]*");

        filterMap.put("nameFilters", nameFilters);
        return filterMap;
    }

    public List<LogFile> getLogFilesByIsDecomposed(String isDecomposed) {
        return logFileDao.getLogFilesByIsDecomposed(isDecomposed);
    }

    public List<LogFile> getLogFilesByIsAnalyzed(String isAnalyzed) {
        return logFileDao.getLogFilesByIsAnalyzed(isAnalyzed);
    }

    public Integer getMaxHourLogFilesByDate(String date) {
        return logFileDao.getMaxHourLogFilesByDate(date);
    }

    @Inject
    public void setLogFileDao(LogFileDao logFileDao) {
        this.logFileDao = logFileDao;
    }
}
