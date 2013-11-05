package com.quidsi.log.analyzing.scheduler.jobs;

import com.quidsi.core.util.StopWatch;
import com.quidsi.log.analyzing.scheduler.service.ActionLogAnalyzedService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * 
 * @author Lumine.Wu
 * 
 */
public class ActionLogAnalyzedJob extends SchedulerJob {
    private final Logger logger = LoggerFactory.getLogger(ActionLogAnalyzedJob.class);
    private ActionLogAnalyzedService clearDataBaseRecordService;

    @Override
    protected void run() {
        StopWatch stopWatch = new StopWatch();
        logger.debug("start analyzing action log");
        clearDataBaseRecordService.setJobName(this.getClass().getSimpleName());
        clearDataBaseRecordService.process();
        logger.debug("end start analyzing action log, time {}ms", stopWatch.elapsedTime());
    }

    @Inject
    public void setClearDataBaseRecordService(ActionLogAnalyzedService clearDataBaseRecordService) {
        this.clearDataBaseRecordService = clearDataBaseRecordService;
    }

}
