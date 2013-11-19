package com.quidsi.log.analyzing.domain;

import java.util.ArrayList;
import java.util.List;

public class LogFileWrapper {

    private Project project;

    private Server server;

    private String startDate;

    private String endDate;

    private String path;

    private ActionLogSchedule actionLogSchedule;

    private final List<LogFile> allLogFiles = new ArrayList<>();

    private final List<LogFile> logFilesHistories = new ArrayList<>();

    public LogFileWrapper(Project project, Server server, String startDate, String endDate, String path) {
        this.project = project;
        this.server = server;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public List<LogFile> getAllLogFiles() {
        return allLogFiles;
    }

    public List<LogFile> getLogFilesHistories() {
        return logFilesHistories;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ActionLogSchedule getActionLogSchedule() {
        return actionLogSchedule;
    }

    public void setActionLogSchedule(ActionLogSchedule actionLogSchedule) {
        this.actionLogSchedule = actionLogSchedule;
    }

}
