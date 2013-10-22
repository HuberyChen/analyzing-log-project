package com.quidsi.log.analyzing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Log_List")
public class APILog {

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int Id;

    @Column(name = "LogName")
    private String logName;

    @Column(name = "APIName")
    private String apiName;

    @Column(name = "HostName")
    private String hostName;

    @Column(name = "LogType")
    private String logType;

    @Column(name = "AbsolutePath")
    private String absolutePath;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

}
