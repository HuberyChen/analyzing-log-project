package com.quidsi.log.analyzing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
        String date = logFileWrapper.getDate();
        int projectId = logFileWrapper.getProject().getId();
        int serverId = logFileWrapper.getServer().getId();
        return logFileDao.getLogFilesByFuzzyName(dateFormat(date), projectId, serverId);
    }

    public List<LogFile> getUncompressedLogFilesByLogFileWrapper(LogFileWrapper logFileWrapper) {
        String date = logFileWrapper.getDate();
        int projectId = logFileWrapper.getProject().getId();
        int serverId = logFileWrapper.getServer().getId();
        return logFileDao.getUncompressedLogFilesByFuzzyName(dateFormat(date), projectId, serverId);
    }

    public List<LogFile> getAnalyzedLogFilesByLogFileWrapper(LogFileWrapper logFileWrapper) {
        String date = logFileWrapper.getDate();
        int projectId = logFileWrapper.getProject().getId();
        int serverId = logFileWrapper.getServer().getId();
        return logFileDao.getAnalyzedLogFilesByFuzzyName(dateFormat(date), projectId, serverId);
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
        pathFilters.add("[\\S\\|\\s]*" + logFileWrapper.getProject().getName() + "[\\S\\|\\s]*");
        pathFilters.add("[\\S\\|\\s]*" + logFileWrapper.getServer().getServerName() + "[\\S\\|\\s]*");
        filterMap.put("pathFilters", pathFilters);

        List<String> nameFilters = new ArrayList<>();
        nameFilters.add("[\\S\\|\\s]*.log[\\S\\|\\s]*");
        nameFilters.add("[\\S\\|\\s]*" + dateFormat(logFileWrapper.getDate()) + "[\\S\\|\\s]*");

        filterMap.put("nameFilters", nameFilters);
        return filterMap;
    }

    private String dateFormat(String date) {
        if (null == date) {
            return "";
        }
        StringBuilder dateString = new StringBuilder();
        String[] dateMessage = date.split("/");
        dateString.append(dateMessage[2]).append("-").append(dateMessage[0]).append("-").append(dateMessage[1]);
        return dateString.toString();
    }

    @Inject
    public void setLogFileDao(LogFileDao logFileDao) {
        this.logFileDao = logFileDao;
    }
}
