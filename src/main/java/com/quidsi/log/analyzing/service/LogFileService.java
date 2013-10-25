package com.quidsi.log.analyzing.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.quidsi.core.util.DateUtils;
import com.quidsi.log.analyzing.dao.LogFileDao;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.LogFileWrapper;

@Service
public class LogFileService {

    private LogFileDao logFileDao;

    @Transactional
    public int save(LogFile logFile) {
        return logFileDao.save(logFile);
    }

    @Transactional
    public void saveList(List<LogFile> logFiles) {
        if (CollectionUtils.isEmpty(logFiles)) {
            return;
        }
        for (LogFile logFile : logFiles) {
            logFileDao.save(logFile);
        }
    }

    @Transactional
    public void update(LogFile logFile) {
        logFileDao.update(logFile);
    }

    public List<LogFile> getLogFilesByLogFileWrapper(LogFileWrapper logFileWrapper) {
        Date date = logFileWrapper.getDate();
        int projectId = logFileWrapper.getProject().getId();
        int serverId = logFileWrapper.getServer().getId();
        return logFileDao.getLogFilesByFuzzyName(dateConverToString(date), projectId, serverId);
    }

    public List<LogFile> getLogFilesByType(String logType) {
        return logFileDao.getLogFileByLogType(logType);
    }

    public LogFile getLogFileByName(String logName, int projectId, int serverId) {
        return logFileDao.getLogFilesByName(logName, projectId, serverId);
    }

    public Map<String, List<String>> initializeFilters(LogFileWrapper logFileWrapper) {
        Map<String, List<String>> filterMap = new HashMap<>();

        if (null == logFileWrapper) {
            return null;
        }

        List<String> pathFilters = new ArrayList<>();
        pathFilters.add("[\\S]*" + logFileWrapper.getProject().getName() + "[\\S]*");
        pathFilters.add("[\\S]*" + logFileWrapper.getServer().getServerName() + "[\\S]*");
        filterMap.put("pathFilters", pathFilters);

        List<String> nameFilters = new ArrayList<>();
        nameFilters.add("[\\S]*.log[\\S]*");
        nameFilters.add("[\\S]*" + dateConverToString(logFileWrapper.getDate()) + "[\\S]*");

        filterMap.put("nameFilters", nameFilters);
        return filterMap;
    }

    public List<LogFile> getLogFilesByIsDecomposed(String isDecomposed) {
        return logFileDao.getLogFilesByIsDecomposed(isDecomposed);
    }

    public List<LogFile> getLogFilesByIsAnalyzed(String isAnalyzed) {
        return logFileDao.getLogFilesByIsAnalyzed(isAnalyzed);
    }

    private String dateConverToString(Date date) {
        if (null == date) {
            return "";
        }
        StringBuilder dateString = new StringBuilder();
        dateString.append(DateUtils.getYear(date)).append("-").append(DateUtils.getMonth(date) + 1).append("-").append(DateUtils.getDay(date));
        return dateString.toString();
    }

    @Inject
    public void setLogFileDao(LogFileDao logFileDao) {
        this.logFileDao = logFileDao;
    }
}
