package com.quidsi.log.analyzing.domain.scheduler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;

/**
 * 
 * @author Lumine.Wu
 * 
 */
@Entity(name = "SchedulerJobTracking")
public class SchedulerJobTracking {
    @Id
    @GeneratedValue
    @Column(name = "TrackingId")
    private int trackingId;

    @Column(name = "JobName", nullable = false, length = 100)
    private String jobName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedTime", nullable = false)
    private Date createdTime;

    @Column(name = "Message", length = 100)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false, length = 30)
    private SchedulerJobStatus status;

    public int getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(int trackingId) {
        this.trackingId = trackingId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SchedulerJobStatus getStatus() {
        return status;
    }

    public void setStatus(SchedulerJobStatus status) {
        this.status = status;
    }

}
