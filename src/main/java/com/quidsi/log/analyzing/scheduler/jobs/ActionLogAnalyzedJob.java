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
    private ActionLogAnalyzedService actionLogAnalyzedService;

    @Override
    protected void run() {
        StopWatch stopWatch = new StopWatch();
        logger.debug("start analyzing action log");
        actionLogAnalyzedService.setJobName(this.getClass().getSimpleName());
        actionLogAnalyzedService.process();
        logger.debug("end start analyzing action log, time {}ms", stopWatch.elapsedTime());
    }

    @Inject
    public void setActionLogAnalyzedService(ActionLogAnalyzedService actionLogAnalyzedService) {
        this.actionLogAnalyzedService = actionLogAnalyzedService;
    }

}
