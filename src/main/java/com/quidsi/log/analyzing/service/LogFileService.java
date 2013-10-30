package com.quidsi.log.analyzing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.quidsi.core.util.StringUtils;
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

    public List<LogFile> getLogFilesByFuzzyName(String date, int projectId, int serverId) {
        return logFileDao.getLogFilesByFuzzyName(dateFormat(date), projectId, serverId);
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
        pathFilters.add(dataMatchAll(projectName));
        pathFilters.add(dataMatchAll(serverName));
        filterMap.put(ServiceConstant.PATHFILTERS, pathFilters);

        List<String> nameFilters = new ArrayList<>();
        nameFilters.add(dataMatchAll(ServiceConstant.LOG_SUFFIX));
        nameFilters.add(dataMatchAll(dateFormat(date)));

        filterMap.put(ServiceConstant.NAMEFILTERS, nameFilters);
        return filterMap;
    }

    private String dataMatchAll(String data) {
        return ServiceConstant.MATCH_ALL + data + ServiceConstant.MATCH_ALL;
    }

    private String dateFormat(String date) {
        if (StringUtils.hasText(date)) {
            StringBuilder dateString = new StringBuilder();
            String[] dateMessage = date.split("/");
            dateString.append(dateMessage[2]).append("-").append(dateMessage[0]).append("-").append(dateMessage[1]);
            return dateString.toString();
        }
        return date;
    }

    @Inject
    public void setLogFileDao(LogFileDao logFileDao) {
        this.logFileDao = logFileDao;
    }
}
