package com.quidsi.log.analyzing.domain.scheduler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;

/**
 * 
 * @author Lumine.Wu
 * 
 */
@Entity(name = "SchedulerJobOperation")
public class SchedulerJobOperation {

    @Id
    @Column(name = "JobName", nullable = false, length = 100)
    private String jobName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LastOperationTime", nullable = false)
    private Date lastOperationTime;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Date getLastOperationTime() {
        return lastOperationTime;
    }

    public void setLastOperationTime(Date lastOperationTime) {
        this.lastOperationTime = lastOperationTime;
    }

}
