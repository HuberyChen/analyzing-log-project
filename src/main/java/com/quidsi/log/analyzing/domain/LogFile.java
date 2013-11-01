package com.quidsi.log.analyzing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

@Entity(name = "Log_File")
public class LogFile {

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int id;

    @Column(name = "LogName")
    @Index(name = "IX_Log_File_Name_Project_Server", columnNames = { "LogName", "ProjectId", "ServerId" })
    private String logName;

    @Column(name = "ProjectId")
    private int projectId;

    @Column(name = "ServerId")
    private int serverId;

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

    public enum IsDecomposed {
        Y, N
    }

    public enum IsAnalyzed {
        Y, N
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
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

}
