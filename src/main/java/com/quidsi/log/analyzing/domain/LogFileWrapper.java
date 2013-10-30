package com.quidsi.log.analyzing.domain;

import java.util.ArrayList;
import java.util.List;

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

    public List<LogFile> getAllLogFiles() {
        return allLogFiles;
    }

    public List<LogFile> getLogFilesHistories() {
        return logFilesHistories;
    }

}
