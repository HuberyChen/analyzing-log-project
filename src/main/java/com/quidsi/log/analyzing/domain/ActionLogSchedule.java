package com.quidsi.log.analyzing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;

@Entity(name = "Schedule")
public class ActionLogSchedule {
    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int id;

    @Column(name = "StartDate")
    private String startDate;

    @Column(name = "EndDate")
    private String endDate;

    @Column(name = "Project")
    private String project;

    @Column(name = "Instance")
    private String instance;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EffectiveStartTime")
    private Date effectiveStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EffectiveEndTime")
    private Date effectiveEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private ScheduleStatus status;

    @Column(name = "Note")
    private String note;

    public static enum ScheduleStatus {
        RUNNING, SUCCESS, ERROR
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(Date effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public Date getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(Date effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    public ScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
