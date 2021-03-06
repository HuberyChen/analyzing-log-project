package com.quidsi.log.analyzing.service;

import com.quidsi.core.util.StringUtils;
import com.quidsi.log.analyzing.dao.LogFileDao;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogFileService {

    private LogFileDao logFileDao;
    private ProjectService projectService;
    private ServerService serverService;

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

    public List<LogFile> getLogFilesByProjectAndServer(String project, String server) {
        return logFileDao.getLogFilesByProjectAndInstance(project, server);
    }

    public List<LogFile> getLogFilesByFuzzyName(String date, String project, String server) {
        return logFileDao.getLogFilesByFuzzyName(date, project, server);
    }

    public List<LogFile> getLogFilesByCondition(String date, String projectName, String serverName) {
        List<LogFile> logFiles = new ArrayList<>();
        if (StringUtils.equals(ServiceConstant.TYPE_ALL, serverName)) {
            Project project = projectService.getProjectByName(projectName);
            if (null == project) {
                throw new IllegalStateException("project not exists");
            }
            List<Server> servers = serverService.getServersByProjectId(project.getId());
            if (CollectionUtils.isEmpty(servers)) {
                throw new IllegalStateException("servers not exist");
            }
            for (Server server : servers) {
                logFiles.addAll(logFileDao.getLogFilesByCondition(date, projectName, server.getServerName()));
            }
        } else {
            logFiles.addAll(logFileDao.getLogFilesByCondition(date, projectName, serverName));
        }

        return logFiles;
    }

    public List<LogFile> getLogFilesByType(String logType) {
        return logFileDao.getLogFilesByLogType(logType);
    }

    public LogFile getLogFileByName(String logName, String project, String server) {
        return logFileDao.getLogFilesByName(logName, project, server);
    }

    public Map<String, List<String>> initializeFilters(String projectName, String serverName, String date) {
        Map<String, List<String>> filterMap = new HashMap<>();

        List<String> pathFilters = new ArrayList<>();
        pathFilters.add(dataMatchAll(projectName));
        pathFilters.add(dataMatchAll(serverName));
        filterMap.put(ServiceConstant.PATHFILTERS, pathFilters);

        List<String> nameFilters = new ArrayList<>();
        nameFilters.add(dataMatchAll(ServiceConstant.LOG_SUFFIX));
        nameFilters.add(dataMatchAll(ServiceConstant.LOG_TYPE_ACTION));
        nameFilters.add(dataMatchAll(date));

        filterMap.put(ServiceConstant.NAMEFILTERS, nameFilters);
        return filterMap;
    }

    private String dataMatchAll(String data) {
        return ServiceConstant.MATCH_ALL + data + ServiceConstant.MATCH_ALL;
    }

    @Inject
    public void setLogFileDao(LogFileDao logFileDao) {
        this.logFileDao = logFileDao;
    }

    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Inject
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }
}
