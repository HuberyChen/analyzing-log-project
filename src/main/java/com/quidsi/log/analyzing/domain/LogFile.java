package com.quidsi.log.analyzing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Log_File")
public class LogFile {

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int id;

    @Column(name = "LogName")
    private String logName;

    @Column(name = "Project")
    private String project;

    @Column(name = "Instance")
    private String instance;

    @Column(name = "LogType")
    private String logType;

    @Column(name = "AbsolutePath")
    private String absolutePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "IsDecomposed")
    private IsDecomposed isDecomposed;

    @Enumerated(EnumType.STRING)
    @Column(name = "IsAnalyzed")
    private IsAnalyzed isAnalyzed;

    public static enum IsDecomposed {
        Y, N
    }

    public static enum IsAnalyzed {
        Y, N
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
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

    public IsDecomposed getIsDecomposed() {
        return isDecomposed;
    }

    public void setIsDecomposed(IsDecomposed isDecomposed) {
        this.isDecomposed = isDecomposed;
    }

    public IsAnalyzed getIsAnalyzed() {
        return isAnalyzed;
    }

    public void setIsAnalyzed(IsAnalyzed isAnalyzed) {
        this.isAnalyzed = isAnalyzed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

}
