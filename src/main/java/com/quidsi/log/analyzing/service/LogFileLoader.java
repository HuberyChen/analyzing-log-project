package com.quidsi.log.analyzing.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.utils.ScanUtils;

@Component
public class LogFileLoader {

    private ProjectService projectService;

    private ServerService serverService;

    private LogFileService logFileService;

    private ActionLogDetailService actionLogDetailService;

    private List<LogFileWrapper> logFileWrappers = new ArrayList<>();

    public void logRead(Date date, String path) {

        loadAllFile(date, path);
        loadActionLogFilesIsExisted();

        logFileService.saveList(getNewLogFiles());

    }

    public void scanProject(String root) {
        List<String> projectNameList = ScanUtils.scanDirectoryFileName(root);
        if (CollectionUtils.isEmpty(projectNameList)) {
            return;
        }
        for (String projectName : projectNameList) {
            if (null == projectService.getProjectByName(projectName)) {
                projectService.save(generateProjectByName(projectName));
            }
        }
    }

    public void scanServer(String root) {
        List<String> pathList = ScanUtils.scanDirectoryFilePath(root);
        if (CollectionUtils.isEmpty(pathList)) {
            return;
        }
        for (String path : pathList) {
            List<String> serverNameList = ScanUtils.scanDirectoryFileName(path);
            Project project = projectService.getProjectByName(ScanUtils.scanPathFileName(path));
            if (CollectionUtils.isEmpty(serverNameList) && null == project) {
                continue;
            }
            for (String serverName : serverNameList) {
                if (null != serverService.getServerByProjectIdAndServerName(project.getId(), serverName)) {
                    continue;
                }
                serverService.save(generateServer(project.getId(), serverName));
            }
        }
    }

    private boolean judgeListIsContainLogFile(LogFile logFile, List<LogFile> logFiles) {
        boolean result = true;
        if (CollectionUtils.isEmpty(logFiles)) {
            result = false;
        }
        for (LogFile contrastLogFile : logFiles) {
            if (judgeLogFileIsEqual(logFile, contrastLogFile)) {
                continue;
            }
            result = false;
        }
        return result;
    }

    private boolean judgeLogFileIsEqual(LogFile logFile, LogFile contrastLogFile) {
        if (!logFile.getLogName().equals(contrastLogFile.getLogName())) {
            return false;
        }
        if (logFile.getProjectId() != contrastLogFile.getProjectId()) {
            return false;
        }
        if (logFile.getServerId() != contrastLogFile.getServerId()) {
            return false;
        }
        return true;
    }

    private List<LogFile> getNewLogFiles() {
        List<LogFile> newLogFiles = new ArrayList<>();
        if (CollectionUtils.isEmpty(logFileWrappers)) {
            return null;
        }
        for (LogFileWrapper logFileWrapper : logFileWrappers) {
            List<LogFile> allLogFiles = logFileWrapper.getAllLogFiles();
            List<LogFile> logFilesIsExisted = logFileWrapper.getLogFilesIsExisted();
            if (CollectionUtils.isEmpty(allLogFiles)) {
                continue;
            }
            for (LogFile logFile : allLogFiles) {
                if (judgeListIsContainLogFile(logFile, logFilesIsExisted)) {
                    continue;
                }
                newLogFiles.add(logFile);
            }
        }
        return newLogFiles;
    }

    private void loadActionLogFilesIsExisted() {
        if (CollectionUtils.isEmpty(logFileWrappers)) {
            return;
        }
        for (LogFileWrapper logFileWrapper : logFileWrappers) {
            List<LogFile> logFilesIsExisted = logFileService.getLogFilesByLogFileWrapper(logFileWrapper);
            logFileWrapper.addLogFilesIsExisted(logFilesIsExisted);
        }
    }

    private void loadAllFile(Date date, String path) {
        initializeLogFileWrapper(date);
        if (CollectionUtils.isEmpty(logFileWrappers)) {
            return;
        }
        for (LogFileWrapper logFileWrapper : logFileWrappers) {
            Map<String, List<String>> filterMap = logFileService.initializeFilters(logFileWrapper);
            List<String> logPaths = ScanUtils.scan(path, filterMap.get("pathFilters"), filterMap.get("nameFilters"));
            logFileWrapper.addAllLogFiles(generateLogFiles(logPaths, logFileWrapper));
        }
    }

    private Project generateProjectByName(String projectName) {
        Project project = new Project();
        project.setName(projectName);
        return project;
    }

    private Server generateServer(int projectId, String serverName) {
        Server server = new Server();
        server.setProjectId(projectId);
        server.setServerName(serverName);
        return server;
    }

    private List<LogFile> generateLogFiles(List<String> logPaths, LogFileWrapper logFileWrapper) {
        List<LogFile> logFiles = new ArrayList<>();
        if (CollectionUtils.isEmpty(logPaths)) {
            return null;
        }
        for (String logPath : logPaths) {
            logFiles.add(generateLogFile(logPath, logFileWrapper.getServer()));
        }
        return logFiles;
    }

    private LogFile generateLogFile(String logPath, Server server) {
        File log = new File(logPath);

        LogFile logFile = generateLogFileByLogName(log.getName());
        logFile.setProjectId(server.getProjectId());
        logFile.setServerId(server.getId());
        logFile.setAbsolutePath(logPath);
        if (logPath.contains(LogFile.IsDecomposed.Y.toString())) {
            logFile.setIsDecomposed(LogFile.IsDecomposed.Y);
        } else {
            logFile.setIsDecomposed(LogFile.IsDecomposed.N);
        }

        if (judgeLogFileIsAnalyzed(logFile)) {
            logFile.setIsAnalyzed(LogFile.IsAnalyzed.Y);
        } else {
            logFile.setIsAnalyzed(LogFile.IsAnalyzed.N);
        }
        return logFile;
    }

    private boolean judgeLogFileIsAnalyzed(LogFile logFile) {
        boolean result = true;
        List<ActionLogDetail> records = actionLogDetailService.getRecordsByLogId(logFile.getId());
        if (CollectionUtils.isEmpty(records)) {
            result = false;
        }
        return result;
    }

    private LogFile generateLogFileByLogName(String logName) {
        LogFile logFile = new LogFile();
        if (logName.endsWith(LogFile.GZ_SUFFIX)) {
            logName = logName.replace(LogFile.GZ_SUFFIX, "");
        }
        if (logName.contains(LogFile.LOG_TYPE_ACTION)) {
            logFile.setLogType(LogFile.LOG_TYPE_ACTION);
        }
        logFile.setLogName(logName.replace(LogFile.LOG_SUFFIX, ""));
        return logFile;
    }

    private void initializeLogFileWrapper(Date date) {
        List<Project> projects = projectService.getProjects();
        if (CollectionUtils.isEmpty(projects)) {
            return;
        }
        for (Project project : projects) {
            List<Server> servers = serverService.getServersByProjectId(project.getId());
            if (CollectionUtils.isEmpty(servers)) {
                continue;
            }
            for (Server server : servers) {
                logFileWrappers.add(new LogFileWrapper(project, server, date));
            }
        }
    }

    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Inject
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    @Inject
    public void setLogFileService(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

    @Inject
    public void setActionLogDetailService(ActionLogDetailService actionLogDetailService) {
        this.actionLogDetailService = actionLogDetailService;
    }

}
