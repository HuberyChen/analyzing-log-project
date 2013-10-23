package com.quidsi.log.analyzing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Log_File")
public class LogFile {

    public final static String LOG_TYPE_ACTION = "action";
    public final static String GZ_SUFFIX = ".gz";
    public final static String LOG_SUFFIX = ".log";
    public final static String DECOMPRESSION = "decompression";

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int Id;

    @Column(name = "LogName")
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

    @Column(name = "Sequence")
    private int sequence;

    public enum IsDecomposed {
        Y, N
    }

    public enum IsAnalyzed {
        Y, N
    }

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

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

}
