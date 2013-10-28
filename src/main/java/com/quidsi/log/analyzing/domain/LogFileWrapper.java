package com.quidsi.log.analyzing.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class LogFileWrapper {

    private Project project;

    private Server server;

    private String date;

    private String path;

    private List<LogFile> allLogFiles = new ArrayList<>();

    private List<LogFile> logFilesHistories = new ArrayList<>();

    public LogFileWrapper(Project project, Server server, String date, String path) {
        this.project = project;
        this.server = server;
        this.date = date;
        this.path = path;
    }

    public void addAllLogFiles(List<LogFile> logFiles) {
        if (CollectionUtils.isEmpty(logFiles)) {
            return;
        }
        for (LogFile logFile : logFiles) {
            allLogFiles.add(logFile);
        }
    }

    public void addLogFilesHistories(List<LogFile> logFiles) {
        if (CollectionUtils.isEmpty(logFiles)) {
            return;
        }
        for (LogFile logFile : logFiles) {
            logFilesHistories.add(logFile);
        }
    }

    public List<LogFile> getLogFileNotExisted() {
        List<LogFile> logFilesNotExisted = new ArrayList<>();
        if (CollectionUtils.isEmpty(allLogFiles)) {
            return null;
        }

        for (LogFile logFile : allLogFiles) {
            if (judgeListIsContainLogFile(logFile, logFilesHistories)) {
                continue;
            }
            logFilesNotExisted.add(logFile);
        }
        return logFilesNotExisted;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<LogFile> getAllLogFiles() {
        return allLogFiles;
    }

    public List<LogFile> getLogFilesHistories() {
        return logFilesHistories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
