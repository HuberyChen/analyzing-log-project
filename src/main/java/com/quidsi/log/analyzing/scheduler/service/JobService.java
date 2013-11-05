package com.quidsi.log.analyzing.scheduler.service;

import com.quidsi.log.analyzing.domain.scheduler.SchedulerJobOperation;
import com.quidsi.log.analyzing.domain.scheduler.SchedulerJobStatus;
import com.quidsi.log.analyzing.scheduler.factory.SchedulerResponseFactory;
import com.quidsi.log.analyzing.utils.TimeConvertUtil;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.util.Date;

/**
 * 
 * @author Lumine.Wu
 * 
 */
@Service
public abstract class JobService {

    private SchedulerJobTrackingService schedulerJobTrackingService;
    private SchedulerResponseFactory schedulerResponseFactory;
    private ProcessJobHelper processJobHelper;
    private Date updateOperationTime;
    private String jobName;

    public void process() {
        updateOperationTime = TimeConvertUtil.getCurrentEasternDate();
        try {
            processJobHelper.processJob(this);
            schedulerJobTrackingService.saveSchedulerJobTracking(jobName, updateOperationTime, schedulerResponseFactory.build(null, SchedulerJobStatus.SUCCEED));
        } catch (Exception e) {
            schedulerJobTrackingService.saveSchedulerJobTracking(jobName, updateOperationTime, schedulerResponseFactory.build(e.getMessage(), SchedulerJobStatus.FAILED));
        }
    }

    protected abstract void processJob(Date updateOperationTime, SchedulerJobOperation schedulerJobOperation);

    @Inject
    public void setSchedulerJobTrackingService(SchedulerJobTrackingService schedulerJobTrackingService) {
        this.schedulerJobTrackingService = schedulerJobTrackingService;
    }

    @Inject
    public void setSchedulerResponseFactory(SchedulerResponseFactory schedulerResponseFactory) {
        this.schedulerResponseFactory = schedulerResponseFactory;
    }

    @Inject
    public void setProcessJobHelper(ProcessJobHelper processJobHelper) {
        this.processJobHelper = processJobHelper;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Date getUpdateOperationTime() {
        return updateOperationTime;
    }
}
