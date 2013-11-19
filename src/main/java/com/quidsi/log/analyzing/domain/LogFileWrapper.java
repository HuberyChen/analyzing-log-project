package com.quidsi.log.analyzing.domain;

import com.quidsi.log.analyzing.utils.TimeConvertUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public List<String> getDateRange() {
        List<String> dates = new ArrayList<>();
        Date start = TimeConvertUtil.stringConvertToDate(startDate);
        Date end = TimeConvertUtil.stringConvertToDate(endDate);
        dates.add(TimeConvertUtil.formatDate(start));
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        boolean bContinue = true;
        while (bContinue) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            if (end.after(cal.getTime())) {
                dates.add(TimeConvertUtil.formatDate(cal.getTime()));
            } else {
                break;
            }
        }
        if (!TimeConvertUtil.formatDate(start).equals(TimeConvertUtil.formatDate(end))) {
            dates.add(TimeConvertUtil.formatDate(end));
        }
        return dates;
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
