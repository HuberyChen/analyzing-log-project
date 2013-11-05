package com.quidsi.log.analyzing.scheduler.service;

import com.quidsi.log.analyzing.domain.scheduler.SchedulerJobStatus;

/**
 * 
 * @author Lumine.Wu
 * 
 */
public class ProcessJobResponse {
    private SchedulerJobStatus status;
    private String message;

    public SchedulerJobStatus getStatus() {
        return status;
    }

    public void setStatus(SchedulerJobStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
