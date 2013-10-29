package com.quidsi.log.analyzing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.quidsi.core.util.StringUtils;
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
    public void saveMap(Map<String, LogFile> logFiles) {
        if (CollectionUtils.isEmpty(logFiles)) {
            return;
        }

        for (Entry<String, LogFile> entry : logFiles.entrySet()) {
            logFileDao.save(entry.getValue());
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
        pathFilters.add(dataMatchAll(logFileWrapper.getProject().getName()));
        pathFilters.add(dataMatchAll(logFileWrapper.getServer().getServerName()));
        filterMap.put("pathFilters", pathFilters);

        List<String> nameFilters = new ArrayList<>();
        nameFilters.add(dataMatchAll(".log"));
        nameFilters.add(dataMatchAll(dateFormat(logFileWrapper.getDate())));

        filterMap.put("nameFilters", nameFilters);
        return filterMap;
    }

    private String dataMatchAll(String data) {
        return "[\\S\\|\\s]*" + data + "[\\S\\|\\s]*";
    }

    private String dateFormat(String date) {
        if (StringUtils.hasText(date)) {
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
