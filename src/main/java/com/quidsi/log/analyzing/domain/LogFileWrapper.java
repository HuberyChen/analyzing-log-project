package com.quidsi.log.analyzing.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class LogFileWrapper {

    private Project project;

    private Server server;

    private Date date;

    private List<LogFile> allLogFiles = new ArrayList<>();

    private List<LogFile> logFilesIsExisted = new ArrayList<>();

    public LogFileWrapper(Project project, Server server, Date date) {
        this.project = project;
        this.server = server;
        this.date = date;
    }

    public void addAllLogFiles(List<LogFile> logFiles) {
        if (CollectionUtils.isEmpty(logFiles)) {
            return;
        }
        for (LogFile logFile : logFiles) {
            allLogFiles.add(logFile);
        }
    }

    public void addLogFilesIsExisted(List<LogFile> logFiles) {
        if (CollectionUtils.isEmpty(logFiles)) {
            return;
        }
        for (LogFile logFile : logFiles) {
            logFilesIsExisted.add(logFile);
        }
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<LogFile> getAllLogFiles() {
        return allLogFiles;
    }

    public List<LogFile> getLogFilesIsExisted() {
        return logFilesIsExisted;
    }

}
