package com.quidsi.log.analyzing.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.CollectionUtils;

public class LogFileWrapper {

    private Project project;

    private Server server;

    private String date;

    private String path;

    private Map<String, LogFile> allLogFiles = new HashMap<>();

    private Map<String, LogFile> logFilesHistories = new HashMap<>();

    public LogFileWrapper(Project project, Server server, String date, String path) {
        this.project = project;
        this.server = server;
        this.date = date;
        this.path = path;
    }

    public void addLogFilesHistories(Map<String, LogFile> logFiles) {
        if (CollectionUtils.isEmpty(logFiles)) {
            return;
        }
        for (Entry<String, LogFile> entry : logFiles.entrySet()) {
            logFilesHistories.put(entry.getKey(), entry.getValue());
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, LogFile> getAllLogFiles() {
        return allLogFiles;
    }

    public Map<String, LogFile> getLogFilesHistories() {
        return logFilesHistories;
    }

}
